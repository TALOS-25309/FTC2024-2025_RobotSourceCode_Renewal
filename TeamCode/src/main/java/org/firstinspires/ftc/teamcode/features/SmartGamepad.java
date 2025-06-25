package org.firstinspires.ftc.teamcode.features;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SmartGamepad {
    private Gamepad gamepadNow, gamepadLast;

    public static class Button {
        private final boolean now;
        private final boolean last;
        public Button(boolean now, boolean last) {
            this.now = now;
            this.last = last;
        }
        public boolean isPressed() { return now && !last; }
        public boolean isReleased() { return !now && last; }
        public boolean isHeld() { return now && last; }
        public boolean isFree() { return !now; }
    }
    public static class Trigger {
        private final double now, last;
        private final double threshold;
        public Trigger(double now, double last, double threshold) {
            this.threshold = threshold;
            this.now = now;
            this.last = last;
        }
        public double getValue() { return now; }
        public double getDifference() { return now - last; }
        public boolean isPressed() { return now > threshold && last <= threshold; }
        public boolean isReleased() { return now <= threshold && last > threshold; }
        public boolean isHeld() { return now > threshold && last > threshold; }
        public boolean isFree() { return now <= threshold; }
    }
    public static class TouchPad {
        public static class Finger {
            private boolean now, last;
            private double x, y, lastx, lasty;
            public Finger(boolean now, boolean last, double x, double y, double lastx, double lasty) {
                this.now = now;
                this.last = last;
                this.x = x;
                this.y = y;
                this.lastx = lastx;
                this.lasty = lasty;
            }
            public boolean isTouched() { return now && !last; }
            public boolean isReleased() { return !now && last; }
            public boolean isOnFinger() { return now && last; }
            public boolean isFree() { return !now; }
            public double getX() { return x; }
            public double getY() { return y; }
            public double getDeltaX() { return x - lastx; }
            public double getDeltaY() { return y - lasty; }
        }
        private Finger finger1, finger2;
        public TouchPad(Finger finger1, Finger finger2) {
            this.finger1 = finger1;
            this.finger2 = finger2;
        }
        public Finger getFinger1() { return finger1; }
        public Finger getFinger2() { return finger2; }
        public boolean isTouched() { return finger1.isTouched() || finger2.isTouched(); }
        public boolean isReleased() { return finger1.isReleased() || finger2.isReleased(); }
        public boolean isOnFinger() { return finger1.isOnFinger() || finger2.isOnFinger(); }
        public boolean isFree() { return finger1.isFree() && finger2.isFree(); }
    }

    private SmartGamepad() {}

    public SmartGamepad(Gamepad gamepad) {
        gamepadNow = gamepad;
        gamepadLast = new Gamepad();
    }

    public Gamepad getCurrentGamepad() {
        return gamepadNow;
    }

    public Gamepad getLastGamepad() {
        return gamepadLast;
    }

    public void update() {
        gamepadLast.copy(gamepadNow);
    }

    public Trigger triggerLeftStickX() {
        return new Trigger(gamepadNow.left_stick_x, gamepadLast.left_stick_x, 0.5);
    }
    public Trigger triggerLeftStickY() {
        return new Trigger(gamepadNow.left_stick_y, gamepadLast.left_stick_y, 0.5);
    }
    public Trigger triggerRightStickX() {
        return new Trigger(gamepadNow.right_stick_x, gamepadLast.right_stick_x, 0.5);
    }
    public Trigger triggerRightStickY() {
        return new Trigger(gamepadNow.right_stick_y, gamepadLast.right_stick_y, 0.5);
    }
    public Button buttonDPadUp() {
        return new Button(gamepadNow.dpad_up, gamepadLast.dpad_up);
    }
    public Button buttonDPadDown() {
        return new Button(gamepadNow.dpad_down, gamepadLast.dpad_down);
    }
    public Button buttonDPadLeft() {
        return new Button(gamepadNow.dpad_left, gamepadLast.dpad_left);
    }
    public Button buttonDPadRight() {
        return new Button(gamepadNow.dpad_right, gamepadLast.dpad_right);
    }
    public Button buttonA() {
        return new Button(gamepadNow.a, gamepadLast.a);
    }
    public Button buttonB() {
        return new Button(gamepadNow.b, gamepadLast.b);
    }
    public Button buttonX() {
        return new Button(gamepadNow.x, gamepadLast.x);
    }
    public Button buttonY() {
        return new Button(gamepadNow.y, gamepadLast.y);
    }
    public Button buttonGuide() {
        return new Button(gamepadNow.guide, gamepadLast.guide);
    }
    public Button buttonStart() {
        return new Button(gamepadNow.start, gamepadLast.start);
    }
    public Button buttonBack() {
        return new Button(gamepadNow.back, gamepadLast.back);
    }
    public Button buttonLeftBumper() {
        return new Button(gamepadNow.left_bumper, gamepadLast.left_bumper);
    }
    public Button buttonRightBumper() {
        return new Button(gamepadNow.right_bumper, gamepadLast.right_bumper);
    }
    public Trigger triggerLeftTrigger() {
        return new Trigger(gamepadNow.left_trigger, gamepadLast.left_trigger, 0.5);
    }
    public Trigger triggerRightTrigger() {
        return new Trigger(gamepadNow.right_trigger, gamepadLast.right_trigger, 0.5);
    }
    public Button buttonCross() {
        return new Button(gamepadNow.cross, gamepadLast.cross);
    }
    public Button buttonCircle() {
        return new Button(gamepadNow.circle, gamepadLast.circle);
    }
    public Button buttonSquare() {
        return new Button(gamepadNow.square, gamepadLast.square);
    }
    public Button buttonTriangle() {
        return new Button(gamepadNow.triangle, gamepadLast.triangle);
    }
    public Button buttonShare() {
        return new Button(gamepadNow.share, gamepadLast.share);
    }
    public Button buttonPS() {
        return new Button(gamepadNow.ps, gamepadLast.ps);
    }
    public Button buttonOptions() {
        return new Button(gamepadNow.options, gamepadLast.options);
    }
    public void rumble(double duration) {
        gamepadNow.rumble((int)(duration * 1000));
    }
    public TouchPad touchpad() {
        TouchPad.Finger finger1 = new TouchPad.Finger(
            gamepadNow.touchpad_finger_1,
            gamepadLast.touchpad_finger_1,
            gamepadNow.touchpad_finger_1_x,
            gamepadNow.touchpad_finger_1_y,
            gamepadLast.touchpad_finger_1_x,
            gamepadLast.touchpad_finger_1_y
        );
        TouchPad.Finger finger2 = new TouchPad.Finger(
            gamepadNow.touchpad_finger_2,
            gamepadLast.touchpad_finger_2,
            gamepadNow.touchpad_finger_2_x,
            gamepadNow.touchpad_finger_2_y,
            gamepadLast.touchpad_finger_2_x,
            gamepadLast.touchpad_finger_2_y
        );
        return new TouchPad(finger1, finger2);
    }

    public boolean atRest() {
        return gamepadNow.atRest();
    }
}
