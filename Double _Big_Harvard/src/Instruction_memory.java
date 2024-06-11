public class Instruction_memory {
    short[] instructionMemory;
    static int noInstructions;

    public Instruction_memory() {

        this.instructionMemory = new short[1024];
    }


    public short[] getInstructionMemory() {
        return instructionMemory;
    }

    public void setInstructionMemory() {
        this.instructionMemory = instructionMemory;
    }

    public short readInstruction(PC pc) {
        if (pc.getProgramCounter() < 1024) {
            short t = instructionMemory[pc.getProgramCounter()];
            pc.setProgramCounter((short) (pc.getProgramCounter() + 1));
            return t;

        } else
            return -1;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < noInstructions; i++) {
            result += instructionMemory[i];
        }
        return result;
    }
}
