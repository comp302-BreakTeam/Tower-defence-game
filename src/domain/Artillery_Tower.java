package domain;


public class Artillery_Tower extends Tower{
	
	public Artillery_Tower() { //creates artillery tower with set variables
		super(10,4f,110);
	}
	
	@Override
    public void upgradeTower() {
		if(this.level<2) {
    	this.range *= 1.2;
    	this.level+=1;
    	}
    }
}
