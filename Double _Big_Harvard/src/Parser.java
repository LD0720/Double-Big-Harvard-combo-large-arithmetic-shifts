import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    ArrayList<String> code = new ArrayList<>();
    ArrayList<String[]> codeSplitted = new ArrayList<String[]>();
    Instruction_memory instruction_memory ;
        public Parser(Architecture architecture){
            instruction_memory = architecture.instruction_memory;
        }

    public void readFile(String fileLocation) {
        //System.out.println("I am reading a file " + fileLocation);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileLocation));
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println("I am a new Instruction");
                //System.out.println(line);
                code.add(line);  //reading line by line and adding it to the code
            }
            reader.close();

            for(String command: code ){
                //System.out.println("I am being splitted");
                codeSplitted.add(command.split(" "));
            }
            Boolean rType = false;
            for (int k = 0 ; k< codeSplitted.size();k++){
                //System.out.println("I am getting interpeted");
                //System.out.println();
                String[] command = codeSplitted.get(k);
                String opcode = "";
               switch (command[0]){
                   case "ADD" :opcode="0000"; rType=true;break;
                   case "SUB" :opcode="0001";rType=true;break;
                   case "MUL" :opcode="0010";rType=true;break;
                   case "MOVI":opcode="0011";rType=false;break;
                   case "BEQZ":opcode="0100";rType=false;break;
                   case "ANDI":opcode="0101";rType=false;break;
                   case "EOR" :opcode="0110";rType=true;break;
                   case "BR"  :opcode="0111";rType=true;break;
                   case "SAL" :opcode="1000";rType=false;break;
                   case "SAR" :opcode="1001";rType=false;break;
                   case "LDR" :opcode="1010";rType=false;break;
                   case "STR" :opcode="1011";rType=false;break;
               }
               String r1 = "";
               for(int i = 1 ; i < command[1].length();i++){
                   //System.out.println(command[1].charAt(i));
                   r1+=command[1].charAt(i);
               }
                //System.out.println("I finished r1");
               int reg1 = Integer.parseInt(r1);
                //System.out.println("reg1 : "+reg1);
               String r1Binary = String.format("%6s", Integer.toBinaryString(reg1)).replace(' ', '0');
                //System.out.println("r1Binary : " + r1Binary);
               String r2Binary = "";
               if(rType) {
                   String r2 = "";
                   //System.out.println("I am Rtype");
                   for (int i = 1; i < command[2].length(); i++) {
                       r2 += command[2].charAt(i);
                   }
                   int reg2 = Integer.parseInt(r2);
                   r2Binary = String.format("%6s", Integer.toBinaryString(reg2)).replace(' ', '0');
               }else{
                   //System.out.println("I am Immediate");
                   //System.out.println(command[2]);
                   String imm = "";
                   for (int i = 0; i < command[2].length(); i++) {
                       imm += command[2].charAt(i);
                   }
                   //System.out.println("Immediate value: " + imm);
                   int immValue = Integer.parseInt(imm);
                   r2Binary = String.format("%6s", Integer.toBinaryString(immValue)).replace(' ', '0');
                   if(immValue<0){
                       r2Binary=r2Binary.substring(r2Binary.length()-1-5,r2Binary.length());
                   }

               }
                //System.out.println("I finished r2");
               String instruction = "";
               instruction = opcode+r1Binary+r2Binary;
                //System.out.println("I am the whole instruction in string format : " + instruction);
               int instruct = Integer.parseInt(instruction,2);
                //System.out.println("Instruction Value : "+ instruct);
                //System.out.println("Instance of instruction memory class "+instruction_memory);
                instruction_memory.instructionMemory[k] = (short) instruct;
                Instruction_memory.noInstructions++;
                //System.out.println("I am added to the instruction memory");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

