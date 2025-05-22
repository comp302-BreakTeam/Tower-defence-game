package domain;


public class Mage_Tower extends Tower{
	
	public Mage_Tower() { //creates mage tower with set variables
		super(10,5f,105);
	}
	@Override
    public void upgradeTower() {
    	if(this.level<2) {
    	this.level+=1;
    	}
    }
}
