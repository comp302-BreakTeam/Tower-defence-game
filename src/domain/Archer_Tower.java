package domain;


public class Archer_Tower extends Tower{
    
    public Archer_Tower() { //creates archer tower with set variables
    	super(10,6f,100); 
    }
    
    @Override
    public void upgradeTower() {
    	if(this.level<2) {
    	this.range*=1.5;
    	this.fireRate = this.fireRate*2;
    	this.level+=1;
    	}
    }
}
