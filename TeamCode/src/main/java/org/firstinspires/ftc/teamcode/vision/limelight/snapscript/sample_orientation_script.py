import cv2
import numpy as np
import math

def transform(img):
    h, w = img.shape[:2]
    src_pts = np.float32([
        [0, 0],         # 좌상
        [w, 0],         # 우상
        [w, h],         # 우하
        [0, h]          # 좌하
    ])
    offset = w * 0.235  # 원하는 만큼 좁힘 (20% 예시)
    dst_pts = np.float32([
        [0, 0],                 # 좌상
        [w, 0],                 # 우상
        [w - offset, h],        # 우하 (x축으로 안쪽)
        [offset, h]             # 좌하 (x축으로 안쪽)
    ])
    matrix = cv2.getPerspectiveTransform(src_pts, dst_pts)
    # 원본과 같은 크기로 출력, 바깥은 자동으로 검은색 처리됨
    result = cv2.warpPerspective(img, matrix, (w, h))
    return result, matrix

def runPipeline(image, llrobot):
    DEBUG = False

    if (DEBUG) :
        llrobot = [0, 0, 0, 0, 0, 0, 0, 0]

    # Filter Values =====================================================================
    yellow_filter = (22, 167, 234), (32, 237, 255)
    blue_filter = (107, 51, 45), (235, 235, 186)
    red_filter = (69, 236, 69), (113, 255, 255)
    #====================================================================================

    # Get values from llrobot
    sample_color_index = int(llrobot[0])
    sample_color = ["yellow ", "blue", "red"][sample_color_index] if sample_color_index >= 0 else "unknown"
    x, y, w, h = map(int, llrobot[1:5])

    llpython = [0, 0, 0, 0, 0, 0, 0, 0]

    if sample_color == "red":
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        hsv[:, :, 0] = (90 + hsv[:, :, 0]) % 180
        image = cv2.cvtColor(hsv, cv2.COLOR_HSV2BGR)

    org_image = image.copy();
    if sample_color == "unknown":
        return np.array([[]]), image, [0, 0, 0, 0, 0, 0, 0, 0]
    filters = [yellow_filter, blue_filter, red_filter]
    filter_low_value, filter_high_value = filters[sample_color_index]


    if DEBUG :
        x___,y___,w___,h___ = contour_test(image, filter_low_value, filter_high_value)
        x,y,w,h = x___,y___,w___,h___

    h_img, w_img = image.shape[:2]
    x_ = x + w
    y_ = y + h
    x = max(0, x)
    y = max(0, y)
    x_ = min(x_, w_img)
    y_ = min(y_, h_img)
    if x >= w_img : x = 0
    if y >= h_img : y = 0
    if x_ <= x : x_ = w_img
    if y_ <= y : y_ = h_img

    # Crop the image by using llrobot data and resize it as original image size
    transformed_img, matrix = transform(image)
    crop_rect = np.array([
        [x, y], [x_, y], [x_, y_], [x, y_]
    ], dtype=np.float32)
    crop_rect_trans = cv2.perspectiveTransform(crop_rect[None, :, :], matrix)[0]

    mask = np.zeros(transformed_img.shape[:2], dtype=np.uint8)
    pts = crop_rect_trans.astype(np.int32)
    cv2.fillPoly(mask, [pts], 255)
    result = cv2.bitwise_and(transformed_img, transformed_img, mask=mask)

    min_x, min_y = np.min(pts, axis=0)
    max_x, max_y = np.max(pts, axis=0)
    crop_img = result

    image = cv2.resize(crop_img, (image.shape[1], image.shape[0]))

    # White if the pixel is in img_threshold, black otherwise
    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    img_threshold = cv2.inRange(img_hsv, filter_low_value, filter_high_value)
    mask = np.zeros_like(image)
    mask[img_threshold > 0] = [255, 255, 255]
    #image = mask

    # Find contour and minrect

    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    img_threshold = cv2.inRange(img_hsv, filter_low_value, filter_high_value)
    contours, _ = cv2.findContours(img_threshold, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    angle = 0

    if len(contours) > 0:
        cnt = max(contours, key=cv2.contourArea)
        rect = cv2.minAreaRect(cnt)
        box = cv2.boxPoints(rect)
        box = np.int0(box)
        (center_x, center_y), (w, h), angle = rect
        cv2.drawContours(image, [box], 0, (0, 0, 255), 2)

        if w < h : angle += 90

        llpython[0] = 1

    print(angle)

    llpython[1] = angle

    if DEBUG:
        llpython[2], llpython[3], llpython[4], llpython[5] = x___,y___,w___,h___

    return np.array([[]]), image, llpython


def contour_test(image, filter_low_value, filter_high_value) :
    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    img_threshold = cv2.inRange(img_hsv, filter_low_value, filter_high_value)

    contours, _ = cv2.findContours(img_threshold,
                                   cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    llpython = [0,0,0,0,0,0,0,0]

    x,y,w,h = 0,0,0,0


    if len(contours) > 0:
        cv2.drawContours(image, contours, -1, 255, 2)
        largestContour = max(contours, key=cv2.contourArea)
        x,y,w,h = cv2.boundingRect(largestContour)

    return x,y,w,h