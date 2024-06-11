public class GPRS  extends Register{


    byte value ;
    Register registers[]=new Register[64];


    public GPRS(byte value) {
        this.value=value;

    }

    public GPRS() {
        for(int i=0;i<registers.length;i++) {
            registers[i]=new Register();
        }

    }
    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public Register[] getRegisters() {
        return registers;
    }

    public void setRegisters(Register[] registers) {
        this.registers = registers;
    }
    public String toString(){
        String result= "";
        for(int i = 0 ; i < 64 ; i++){
            result += i + " " + this.registers[i].toString() +"\n" ;
        }
        return result;
    }
}
