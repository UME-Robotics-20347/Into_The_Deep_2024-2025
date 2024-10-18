package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// Math.abs() Now we have a six pack
@TeleOp(name="Clank  TeleOp", group="TeleOp Bread")
public class ClanketyTeleOp extends LinearOpMode {
    //declare and initialize constants
    static double CHASSIS_SPEED = 0.5;
    double frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {
        Hardware.init(this.hardwareMap);
        Hardware.wrist.setPower(1);
        sleep(1000);

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

            if (gamepad2.dpad_up) {
                Hardware.linearSlide.setPower(-0.5);
            } else if (gamepad2.dpad_down){
                Hardware.linearSlide.setPower(0.5);
            } else {
                Hardware.linearSlide.setPower(0);
            }

            /*
            if ((Math.signum(gamepad2.left_stick_y) != Math.signum(Hardware.linearSlide.getCurrentPosition())) || (Math.abs(Hardware.linearSlide.getCurrentPosition()) < 600)) {
                Hardware.linearSlide.setPower(gamepad2.left_stick_y * 0.75);
            } else {
                Hardware.linearSlide.setPower(0);
            }
            */

            //Joke stuffs
            //x = x + 1
            // Java response: I don't see the problem vs. Mathmetician response: AHHHHHHHHHHHH!

            /*
            if(gamepad2.a) {
                Hardware.drone.setPower(0.3);
            }
            */

            telemetry.addData("drone", Hardware.drone.getDirection());

            telemetry.addData("linear slide position", Hardware.linearSlide.getCurrentPosition());

            telemetry.addData("left stick y", gamepad2.left_stick_y);

            telemetry.update();
        }
    }
    public void findChassisPower() {
        if (gamepad1.x) {
            CHASSIS_SPEED = 0.5;
        } else if (gamepad1.b) {
            CHASSIS_SPEED = 1;
        } else if (gamepad1.y) {
            CHASSIS_SPEED = 0.75;
        } else if(gamepad1.a) {
            CHASSIS_SPEED = 0.25;
        }

        telemetry.addData("speed: ",CHASSIS_SPEED);

        //strafing motor code
        /*
        frontLeft = CHASSIS_SPEED * (gamepad1.left_stick_y - gamepad1.left_stick_x);
        frontRight = CHASSIS_SPEED * -(gamepad1.right_stick_y - gamepad1.right_stick_x);
        backLeft = CHASSIS_SPEED * (gamepad1.left_stick_y + gamepad1.left_stick_x);
        backRight = CHASSIS_SPEED * -(gamepad1.right_stick_y + gamepad1.right_stick_x);
         */


        frontLeft = CHASSIS_SPEED * (gamepad1.left_stick_y);
        frontRight = CHASSIS_SPEED * -(gamepad1.right_stick_y);
        backLeft = CHASSIS_SPEED * (gamepad1.left_stick_y);
        backRight = CHASSIS_SPEED * -(gamepad1.right_stick_y);

        //make sure motors don't power > 1, maintain proportions
        double max =
            Math.max(
                Math.max(
                        Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                        Math.max(Math.abs(backLeft), Math.abs(backRight))
                ),
                1
            );

        frontLeft /= max;
        frontRight /= max;
        backLeft /= max;
        backRight /= max;
    }
}