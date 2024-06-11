public class Main {


    public static void main(String[] args) {
        int clkCycle = 1;
        Architecture architecture;
        int fetched;
        Object[] decoded;
        architecture = new Architecture();
        Boolean isFetched = false;
        Boolean isDecoded = false;
        Boolean isExecuted = false;
        int fetchedData = 0;
        Object[] decodedData = null;
        Boolean f , d ;
        f = false;
        d = false;
        int tempF = 0;
        Object[] tempD =null;
        architecture.parser.readFile("testCases.txt");
        //System.out.println("I am all the instructions that are parsed");
        //System.out.println(architecture.instruction_memory.instructionMemory.toString());
        //System.out.println("--------------------------------------------------------------------------------------------------------");
        while (architecture.pc.getProgramCounter() < Instruction_memory.noInstructions+2) {
            System.out.println("Clock="+ clkCycle);
            System.out.println("pc "+ architecture.pc.programCounter);

            fetchedData = architecture.fetch();
            f= true;
            System.out.println("Fetched Data = " + fetchedData);

            if (isFetched) {
                decodedData = architecture.decode(tempF);
                System.out.println("Decoded opcode  = " + ((int)decodedData[0]));
                System.out.println("Decoded R1 = " + ((int)decodedData[1]));
                System.out.println("Decoded R2 = " + ((int)decodedData[2]));
                System.out.println("Decoded immediate = " + ((int)decodedData[3]));
                System.out.println("address = " + ((int)decodedData[4]));
                d = true;
            }
            if (isDecoded) {
                architecture.execute(tempD);
                //System.out.println("Execution occured");
                isExecuted = true;
            }
            if(f & d) {
                isFetched = true;
                isDecoded = true;
            }
            else if (f){
                isFetched = true;
            }
            tempF = fetchedData;
            tempD = decodedData;
            if(isExecuted) {
                if (architecture.branch) {
                    //System.out.println("I should branch");
                    //System.out.println("The new PC Value is : " + architecture.pc.getProgramCounter());
                    fetchedData = 0;
                    decodedData = null;
                    tempF = 0;
                    tempD = null;
                    isFetched = false;
                    isDecoded = false;
                    f=false;
                    d=false;
                    architecture.branch = false;
                   // architecture.pc.setProgramCounter((short) 0);
                }
            }
            //System.out.println("The registers data : ");
            //System.out.println(architecture.general_purpose_register.toString());
            //System.out.println(clkCycle);
            System.out.println("********************************************************");
            clkCycle++;

        }
        System.out.println("Registers data "+ architecture.general_purpose_register.toString());
        System.out.println("printing memory "+ architecture.data_memory.toString());
        //System.out.println("Instruction 12 : "+ architecture.instruction_memory.instructionMemory[11]);
        //System.out.println(architecture.general_purpose_register.toString());
        //System.out.println("Data Memory : " + architecture.data_memory.toString());
        //System.out.println("Instruction 17 : " + architecture.instruction_memory.instructionMemory[15]);

    }

    //make an instance of parser calss giving it the file path/location to fill the instruction memory
    //enter the main method
    //loop over the instructions
    //put for each operation a flag to keep track of cycles
    //fetch/decode/execute for each instruction


}