public class Register {


    byte value;


    public Register(byte value) {
        this.value = value;
    }

    public Register() {

    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
    public String toString(){
        return this.value + "";
    }

}
