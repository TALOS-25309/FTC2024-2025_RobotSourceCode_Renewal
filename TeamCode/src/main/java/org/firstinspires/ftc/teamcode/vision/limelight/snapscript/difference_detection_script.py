import cv2
import numpy as np

last_img = None

test = 0
test_screen = None

def runPipeline(img, llrobot):
    global last_img
    global test, test_screen

    llpython = [0,0,0,0,0,0,0,0]

    if llrobot[0] == 0.0:
        test += 1
        if test > 100 :
            test = 0
        if test != 0 :
            return np.array([[]]), test_screen, llpython

    org_img = img

    threshold = 50
    left_ratio = 0.1
    right_ratio = 0.1
    top_ratio = 0.7
    bottom_ratio = 0.0

    h, w = img.shape[:2]
    x1 = int(w * left_ratio)
    x2 = int(w * (1 - right_ratio))
    y1 = int(h * top_ratio)
    y2 = int(h * (1 - bottom_ratio))
    #img = img[y1:y2, x1:x2]
    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    if last_img is None:
        last_img = img

    diff = cv2.absdiff(img, last_img)
    mask = diff >= threshold
    count = np.count_nonzero(mask)

    print(count)

    llpython[0] = llrobot[0]
    llpython[1] = count

    if llrobot[0] == 0.0 or llrobot[0] == 1.0:
        last_img = img

    # print target
    img = diff

    screen = center_image_on_black_bg(img)
    screen = cv2.cvtColor(screen, cv2.COLOR_GRAY2BGR)
    test_screen = screen
    return np.array([[]]), screen, llpython

def center_image_on_black_bg(src_img, target_size=(640, 480)):
    tgt_w, tgt_h = target_size
    src_h, src_w = src_img.shape[:2]

    if len(src_img.shape) == 2:
        bg = np.zeros((tgt_h, tgt_w), dtype=src_img.dtype)
    else:
        bg = np.zeros((tgt_h, tgt_w, src_img.shape[2]), dtype=src_img.dtype)

    x_offset = (tgt_w - src_w) // 2
    y_offset = (tgt_h - src_h) // 2

    bg[y_offset:y_offset+src_h, x_offset:x_offset+src_w] = src_img
    return bg