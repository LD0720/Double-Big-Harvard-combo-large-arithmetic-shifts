import java.util.HashMap;

public class Data_memory{

        byte dataMemory[];

    public Data_memory() {
        this.dataMemory = new byte[2048];
    }

    public byte readData(short address) {
            if (address>=0 && address<=2047) {
                byte t = dataMemory[address];
                return t;
            }
            else {
                return -1;
            }

        }

        public void writeData(short address,byte data) {
            if(address>=0 && address<=2047) {
                dataMemory[address] = data;
            }
            else {
                //System.out.println("address out of range !");
            }

        }
    public String toString(){
        String result = "";
        for(int i = 0 ; i< 127 ; i++){
            result += dataMemory[i]+"\n";
        }
        return result;
    }
    }

