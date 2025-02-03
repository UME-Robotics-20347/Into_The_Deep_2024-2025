package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// Math.abs(6) Now we have a six pack
@TeleOp(name="Clank  TeleOp", group="TeleOp Bread")
public class ClanketyTeleOp extends LinearOpMode {
    //declare and initialize constants
    double CHASSIS_SPEED = 0.5;
    double frontLeftP, frontRightP, backLeftP, backRightP;// backLeftP = back left power etc...
    boolean pressedHang;

    @Override
    public void runOpMode() {
        Hardware.init(hardwareMap);
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.ascent2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(1000);

        waitForStart();
        while (opModeIsActive()) {
            findChassisPower();
            Hardware.frontRight.setPower(frontRightP);
            Hardware.backRight.setPower(backRightP);
            Hardware.frontLeft.setPower(frontLeftP);
            Hardware.backLeft.setPower(backLeftP);

            if(gamepad2.y) {
                Hardware.wrist.setPosition(0);
                //Hardware.wrist.setPower(-0.1);
            } else if (gamepad2.b) {
                Hardware.wrist.setPosition(0.5);
                //Hardware.wrist.setPower(0.75);
            }

            if (gamepad2.x) {
                Hardware.inTake.setPower(1);
            } else if (gamepad2.a) {
                Hardware.inTake.setPower(-1);
            } else {
                Hardware.inTake.setPower(0);
            }

            //add trigger controls for arm speed for hanging

            if (gamepad2.dpad_up) {
                Hardware.armMotor.setPower(-0.75);
            } else if (gamepad2.dpad_down){
                Hardware.armMotor.setPower(0.75);
            } else if (!pressedHang) {
                Hardware.armMotor.setPower(0);
            }

            if (gamepad2.dpad_left) {
                pressedHang = true;
            } else if (gamepad2.dpad_right) {
                pressedHang = false;
            }

            if (pressedHang) {
                Hardware.armMotor.setPower(1);
            }

            Hardware.ascent2.setPower(gamepad2.left_stick_y);

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

            //telemetry.addData("drone", Hardware.drone.getDirection());

            telemetry.addData("linear slide position", Hardware.armMotor.getCurrentPosition());

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


        frontLeftP = CHASSIS_SPEED * (gamepad1.left_stick_y);
        frontRightP = CHASSIS_SPEED * -(gamepad1.right_stick_y);
        backLeftP = CHASSIS_SPEED * (gamepad1.left_stick_y);
        backRightP = CHASSIS_SPEED * -(gamepad1.right_stick_y);

        //make sure motors don't power > 1, maintain proportions
        double max =// Integer.MAX_VALUE;
            Math.max(
                Math.max(
                        Math.max(Math.abs(frontLeftP), Math.abs(frontRightP)),
                        Math.max(Math.abs(backLeftP), Math.abs(backRightP))
                ),
                1
            );

        frontLeftP /= max;
        frontRightP /= max;
        backLeftP /= max;
        backRightP /= max;
    }
}