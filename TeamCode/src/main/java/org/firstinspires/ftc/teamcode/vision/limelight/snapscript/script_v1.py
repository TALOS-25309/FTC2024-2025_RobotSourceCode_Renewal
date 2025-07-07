import cv2
import numpy as np
import math

def realCoordinate(x, y, camera_angle_x, camera_angle_y, h) :
    theta_h = math.radians(54.505)
    theta_v = math.radians(42.239)

    phi_h = math.radians(camera_angle_x);
    phi_v = math.radians(90 - camera_angle_y);

    phi_h_prime = phi_h + math.atan(x * math.tan(theta_h / 2)) + math.radians(0.1)
    phi_v_prime = phi_v - math.atan(y * math.tan(theta_v / 2)) - math.radians(0.95)

    new_x = h / math.tan(phi_v_prime) * math.cos(phi_h_prime)
    new_y = h / math.tan(phi_v_prime) * math.sin(phi_h_prime)
    return new_x, new_y

def runPipeline(image, llrobot):
    DEBUG = True

    if (DEBUG) :
        llrobot = [0, 0, 0, 0, 0, 0, 0, 14.2 - 1.9]

    amplifier = 20.0;

    # Filter Values =====================================================================
    yellow_filter = (6, 34, 140), (30, 255, 255)
    blue_filter = (107, 51, 45), (235, 235, 186)
    red_filter = (69, 236, 69), (113, 255, 255)
    #====================================================================================

    # Get values from llrobot
    sample_color_index = int(llrobot[0])
    sample_color = ["yellow ", "blue", "red"][sample_color_index] if sample_color_index >= 0 else "unknown"
    x, y, w, h = map(int, llrobot[1:5])
    camera_angle_x = llrobot[5]
    camera_angle_y = llrobot[6]
    height = llrobot[7]
    if camera_angle_y <= 0 : camera_angle_y = 90

    llpython = [0, 0, 0, 0, 0, 0, 0, 0]

    if sample_color == "red":
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        hsv[:, :, 0] = (90 + hsv[:, :, 0]) % 180
        image = cv2.cvtColor(hsv, cv2.COLOR_HSV2BGR)

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

    org_image = image.copy();

    center_x = (x + x_) / 2
    center_y = 480 - (y + y_) / 2

    if sample_color == "unknown":
        return np.array([[]]), image, [0, 0, 0, 0, 0, 0, 0, 0]

    filters = [yellow_filter, blue_filter, red_filter]

    filter_low_value, filter_high_value = filters[sample_color_index]

    #'''
    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    img_threshold = cv2.inRange(img_hsv, filter_low_value, filter_high_value)

    contours, _ = cv2.findContours(img_threshold,
                                   cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    largestContour = np.array([[]])
    llpython = [0,0,0,0,0,0,0,0]

    x___,y___,w___,h___ = 0,0,0,0

    if len(contours) > 0:
        cv2.drawContours(image, contours, -1, 255, 2)
        largestContour = max(contours, key=cv2.contourArea)
        x___,y___,w___,h___ = cv2.boundingRect(largestContour)
    #'''

    # Crop the image by using llrobot data and resize it as original image size
    crop_img = image[y:y_, x:x_]
    target_h, target_w = image.shape[:2]
    crop_h, crop_w = crop_img.shape[:2]
    scale = min(target_w / crop_w, target_h / crop_h)
    resized_w = int(crop_w * scale)
    resized_h = int(crop_h * scale)
    resized_crop = cv2.resize(crop_img, (resized_w, resized_h))
    image = np.zeros((target_h, target_w, 3), dtype=np.uint8)
    x_offset = (target_w - resized_w) // 2
    y_offset = (target_h - resized_h) // 2
    image[y_offset:y_offset + resized_h, x_offset:x_offset + resized_w] = resized_crop

    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    img_threshold = cv2.inRange(img_hsv, filter_low_value, filter_high_value)

    # White if the pixel is in img_threshold, black otherwise
    mask = np.zeros_like(image)
    mask[img_threshold > 0] = [255, 255, 255]
    image = mask

    # fitLine
    ys, xs = np.where(image[:, :, 0] == 255)
    if len(xs) > 100:
        points = np.column_stack((xs, ys)).astype(np.int32)
        if (DEBUG) :
            mean_point = np.mean(points, axis = 0)
            center_x, center_y = mean_point
        [vx, vy, x0, y0] = cv2.fitLine(points, cv2.DIST_L2, 0, 0.01, 0.01)
        h, w = image.shape[:2]
        left_y = (-x0 * vy / vx) + y0
        right_y = ((w - x0) * vy / vx) + y0
        pt1 = (0, int(left_y))
        pt2 = (w - 1, int(right_y))
        cv2.line(image, pt1, pt2, (0, 0, 255), 2)

        ry1, rx1 = realCoordinate((center_x + vx * amplifier) / (target_w/2) - 1, (center_y + vy * amplifier) / (target_h/2) - 1, camera_angle_x, camera_angle_y, height);
        ry2, rx2 = realCoordinate((center_x - vx * amplifier) / (target_w/2) - 1, (center_y - vy * amplifier) / (target_h/2) - 1, camera_angle_x, camera_angle_y, height);

        pt1 = (int(center_x+vx * amplifier), int(center_y+vy * amplifier))
        cv2.circle(image, pt1, 5, (255, 0, 0), -1)
        pt2 = (int(center_x-vx * amplifier), int(center_y-vy * amplifier))
        cv2.circle(image, pt2, 5, (255, 0, 0), -1)

        vx = rx2 - rx1;
        vy = ry2 - ry1;

        #print(camera_angle_y)
        #print(rx1, ry1, rx2, ry2)
        #print(vx, vy)

        left_y = (-center_x * vy / vx) + center_y
        right_y = ((w - center_x) * vy / vx) + center_y
        pt1 = (0, int(left_y))
        pt2 = (w - 1, int(right_y))
        cv2.line(image, pt1, pt2, (0, 255, 0), 2)

        angle = math.degrees(math.atan2(vx, vy))
        llpython[0] = 1
        llpython[1] = angle

        #print("ANGLE : " + str(angle))



    #'''
    llpython[2], llpython[3], llpython[4], llpython[5] = x___,y___,w___,h___
    #'''
    return np.array([[]]), image, llpython