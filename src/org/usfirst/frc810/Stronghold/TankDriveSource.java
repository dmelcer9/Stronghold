package org.usfirst.frc810.Stronghold;

public enum TankDriveSource {
	JOYSTICKS, GAMEPAD;
	
	
	public double getLeftValue(){
		switch(this){
		case JOYSTICKS:
			return Robot.oi.getLeftDrive().getY();
		case GAMEPAD:
			return Robot.oi.getGamepad().getRawAxis(1);
		default:
			return 0;
		}
		
	}
	
	public double getRightValue(){
		switch(this){
		case JOYSTICKS:
			return Robot.oi.getRightDrive().getY();
		case GAMEPAD:
			return Robot.oi.getGamepad().getRawAxis(3);
		default:
			return 0;
		}
	}
}
