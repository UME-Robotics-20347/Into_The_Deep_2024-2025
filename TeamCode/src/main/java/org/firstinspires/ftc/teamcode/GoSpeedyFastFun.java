package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Go Speedy Fast TeleOp", group="TeleOp Bread")
public class GoSpeedyFastFun extends LinearOpMode {
    //declare and initialize constants
    static double CHASSIS_SPEED = 1;
    double frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {
        Hardware.init(this.hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            findChassisPower();
            Hardware.frontRight.setPower(frontRight);
            Hardware.backRight.setPower(backRight);
            Hardware.frontLeft.setPower(frontLeft);
            Hardware.backLeft.setPower(backLeft);

            if(gamepad2.y) {
                Hardware.wrist.setPower(0);
            } else if (gamepad2.b) {
                Hardware.wrist.setPower(1);
            }

            if (gamepad2.x) {
                Hardware.inTake.setPower(1);
            } else if (gamepad2.a) {
                Hardware.inTake.setPower(-1);
            } else {
                Hardware.inTake.setPower(0);
            }

            if ((Math.signum(gamepad1.right_trigger - gamepad1.left_trigger) != Math.signum(Hardware.linearSlide.getCurrentPosition()) ) || (Math.abs(Hardware.linearSlide.getCurrentPosition()) < 580))
                Hardware.linearSlide.setPower((gamepad1.right_trigger - gamepad1.left_trigger) * 1);
            else
                Hardware.linearSlide.setPower(0);

            telemetry.addData("linear slide position", Hardware.linearSlide.getCurrentPosition());

            telemetry.addData("linear slide speed", Hardware.linearSlide.getPower());

            telemetry.update();
        }
    }
    public void findChassisPower() {
        frontLeft = CHASSIS_SPEED * (gamepad1.left_stick_y - gamepad1.left_stick_x);
        frontRight = CHASSIS_SPEED * (gamepad1.right_stick_y - gamepad1.right_stick_x);
        backLeft = CHASSIS_SPEED * (gamepad1.left_stick_y + gamepad1.left_stick_x);
        backRight = CHASSIS_SPEED * (gamepad1.right_stick_y + gamepad1.right_stick_x);
    }
}