public class Status_register  extends Register{
    byte value;
    int carryFlag;
    int overflowFlag;
    int negativeFlag;
    int signFlag;
    int zeroFlag;

    int[] statusValue= new int[8];//array containing each value of in the 8 bit status register




    public Status_register(byte value) {

       this.value=value;
    }

    public Status_register() {
        super();
    }

    public void statusValue (){
        statusValue[0]=zeroFlag;
        statusValue[1]=signFlag;
        statusValue[2]=negativeFlag;
        statusValue[3]=overflowFlag;
        statusValue[4]=carryFlag;
        statusValue[5]=0;
        statusValue[6]=0;
        statusValue[7]=0;

    }

    public int[] getStatus() {
        return statusValue;
    }

    public void setStatus(int[] status) {
        this.statusValue = status;
    }


    public int getCarryFlag() {
        return carryFlag;
    }

    public void setCarryFlag(int carryFlag) {
        this.carryFlag = carryFlag;
    }

    public int getOverflowFlag() {
        return overflowFlag;
    }

    public void setOverflowFlag(int overflowFlag) {
        this.overflowFlag = overflowFlag;
    }

    public int getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(int negativeFlag) {
        this.negativeFlag = negativeFlag;
    }

    public int getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(int signFlag) {
        this.signFlag = signFlag;
    }

    public int getZeroFlag() {
        return zeroFlag;
    }

    public void setZeroFlag(int zeroFlag) {
        this.zeroFlag = zeroFlag;
    }
}
