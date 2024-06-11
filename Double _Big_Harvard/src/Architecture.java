public class Architecture {
    Instruction_memory instruction_memory;
    Data_memory data_memory;
    GPRS general_purpose_register;
    Status_register status_register;
    PC pc;
    Parser parser;
    Boolean branch;

    public Architecture() {
        this.pc = new PC();
        data_memory = new Data_memory();
        instruction_memory = new Instruction_memory();
        status_register = new Status_register();
        general_purpose_register = new GPRS();
        parser = new Parser(this);

    }

    public int fetch() {
        //System.out.println("pc value during fetching : " + pc.getProgramCounter());
        short result = instruction_memory.readInstruction(pc);
        //System.out.println("value of fetch method : " + result);
        return result;

    }


    public Object[] decode(int instruction) {
        int opcode = 0;  // bits15:12
        int r1 = 0;      // bits11:6
        int r2 = 0;      // bit5:0
        int imm = 0;     // bits5:0
        int address = 0; // bits10:0

        // Complete the decode() body...
        opcode = instruction >>> 12;
        opcode = opcode & 15;

        r1 = instruction >>> 6;
        r1 = r1 & 63;
        //System.out.println("I am r1 value while decoding : " + r1);

        r2 = instruction & 63;
        //System.out.println("r2 value in decode : " + r2);

        imm = instruction;
        imm = imm & 63;

        address = instruction;
        address = address & 63;
        Register reg1 = general_purpose_register.getRegisters()[r1];
        Register reg2 = general_purpose_register.getRegisters()[r2];
        Object[] instruct = {opcode, r1, r2, imm, address};
        return instruct;
    }

    public byte signExtend(byte immediate) {
        int sign = immediate >> 5;
        if (sign == 1) {
            int signExtendedValue = immediate | 192;
            immediate = (byte) signExtendedValue;
        }
        return immediate;
    }

    public void execute(Object[] instruction) {
        Register r1 = general_purpose_register.getRegisters()[(int) instruction[1]];
        Register r2 = general_purpose_register.getRegisters()[(int) instruction[2]];
        //System.out.println("r2 value in execute method : "+ r2.getValue());
        int imm = (int) instruction[3];
        switch ((int) instruction[0]) {
            case 0:
                r1.setValue(ADD(r1, r2));
                System.out.println("I am adding data" + r1.getValue()+" "+r2.getValue() );
                branch = false;
                break;
            case 1:
                r1.setValue(SUB(r1, r2));
                System.out.println("I am SUB data" + r1.getValue()+" "+ r2.getValue() );
                branch = false;
                break;
            case 2:
                r1.setValue(MUL(r1, r2));
                System.out.println("I am MUL data" + r1.getValue() + " " + r2.getValue());
                branch = false;
                break;
            case 3:
                r1.setValue(MOVI(r1, signExtend((byte) imm)));
                branch = false;
                System.out.println("I am MOVI data " + r1.getValue() + " "+signExtend((byte) imm));
                break;
            case 4:
                pc.setProgramCounter(BEQZ(r1, signExtend((byte) imm)));
                branch = true;
                System.out.println("BEQZ instruction changed pc value to " + pc.getProgramCounter());
                break;
            case 5:
                r1.setValue(ANDI(r1, signExtend((byte) imm)));
                branch = false;
                System.out.println("I am ANDI data " + r1.getValue()+" "+signExtend((byte) imm));
                break;
            case 6:
                r1.setValue(EOR(r1, r2));
                branch = false;
                System.out.println("I am EOR data "+ r1.getValue() +" " + r2.getValue() );
                break;
            case 7:
                pc.setProgramCounter(BR(r1, r2));
                System.out.println("PC in BR " + r1.getValue() + " "+ r2.getValue());
                branch = true;
                System.out.println("BR instruction changed pc value to "+ pc.getProgramCounter());
                break;
            case 8:
                r1.setValue(SAL(r1, signExtend((byte) imm)));
                branch = false;
                System.out.println("I am the shifting left of "+ " "+r1.getValue() + " by " + signExtend((byte) imm));
                break;
            case 9:
                r1.setValue(SAR(r1, signExtend((byte) imm)));
                branch = false;
                System.out.println("I am SAR data " + r1.getValue()+ " "+signExtend((byte) imm));
                break;
            case 10:
                r1.setValue(LDR(r1, Short.parseShort(String.valueOf(instruction[4]))));
                System.out.println("Entered LDR Case "+r1.toString()+" "+Short.parseShort(String.valueOf(instruction[4])));
                branch = false;
                break;
            case 11:
                STR(r1, Short.parseShort(String.valueOf(instruction[4])));
                System.out.println("ENTERED STR CASE");
                System.out.println("address : " + Short.parseShort(String.valueOf(instruction[4])));
                System.out.println("Data Memory: " + data_memory.toString());
                branch = false;
                break;
        }

    }

    public byte ADD(Register R1, Register R2) {
        // System.out.println("R1 VALUE : " + R1.getValue());
        //System.out.println("R2 VALUE : " + R2.getValue());
        int resultofadding = (R1.getValue() + R2.getValue());
        //System.out.println("Result of adding : "+ resultofadding);
        if ((resultofadding & 256) == 256) {
            status_register.setCarryFlag(1);
        } else {
            status_register.setCarryFlag(0);
        }
        if ((R1.getValue() > 0 && R2.getValue() > 0 && resultofadding < 0) || (R1.getValue() < 0 && R2.getValue() < 0 && resultofadding > 0)) {
            status_register.setOverflowFlag(1);
        } else
            status_register.setOverflowFlag(0);
        if (resultofadding < 0) {
            status_register.setNegativeFlag(1);
        } else {
            status_register.setNegativeFlag(0);
        }
        status_register.setSignFlag(status_register.getOverflowFlag() ^ status_register.getNegativeFlag());
        if (resultofadding == 0)
            status_register.setZeroFlag(1);
        else
            status_register.setZeroFlag(0);

        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return (byte) resultofadding;
    }

    public byte SUB(Register R1, Register R2) {
        int result = (R1.getValue() - R2.getValue());

        if ((R1.getValue() > 0 && R2.getValue() < 0 && result < 0) || (R1.getValue() < 0 && R2.getValue() > 0 && result > 0)) {
            status_register.setOverflowFlag(1);
        } else
            status_register.setOverflowFlag(0);
        if (result < 0) {
            status_register.setNegativeFlag(1);
        } else {
            status_register.setNegativeFlag(0);
        }
        status_register.setSignFlag(status_register.getOverflowFlag() ^ status_register.getNegativeFlag());
        if (result == 0)
            status_register.setZeroFlag(1);
        else
            status_register.setZeroFlag(0);

        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return (byte) result;
    }

    public byte MUL(Register R1, Register R2) {
        int result = (R1.getValue() * R2.getValue());

        if (result < 0) {
            status_register.setNegativeFlag(1);
        } else {
            status_register.setNegativeFlag(0);
        }

        if (result == 0)
            status_register.setZeroFlag(1);
        else
            status_register.setZeroFlag(0);

        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return (byte) result;
    }

    public byte MOVI(Register R1, byte IMM) {
        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return IMM;
    }

    public short BEQZ(Register R1, byte IMM) {
        if (R1.getValue() == 0) {
            //System.out.println("I am the old pc "+ pc.getProgramCounter());
            //System.out.println("I am the immediate value in branch" + IMM);
            //System.out.println("new PC value: " + (pc.getProgramCounter() -4  + 1 + IMM  ));

            return (short) (pc.getProgramCounter() - 4 + 1 + IMM);
        }

        return -1;
    }

    public byte ANDI(Register R1, byte IMM) {
        int result = (R1.getValue() & IMM);
        //System.out.println("result of anding: " + result );
        if (result < 0) {
            status_register.setNegativeFlag(1);
        } else {
            status_register.setNegativeFlag(0);
        }

        if (result == 0)
            status_register.setZeroFlag(1);
        else
            status_register.setZeroFlag(0);

        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return (byte) result;
    }

    public byte EOR(Register R1, Register R2) {
        byte result = (byte) (R1.getValue() ^ R2.getValue());
        if (result == 0) {
            status_register.setZeroFlag(1);
        }
        else {
            status_register.setZeroFlag(0);
        }
        if (result < 0) {
            status_register.setNegativeFlag(1);
        }
        else {
            status_register.setZeroFlag(0);
        }

        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return result;
    }

    public short BR(Register R1, Register R2) {
        //System.out.println("Entered BR");
        String r1Value = String.format("%4s", Integer.toBinaryString(R1.getValue())).replace(' ', '0');
        String r2Value = String.format("%4s", Integer.toBinaryString(R2.getValue())).replace(' ', '0');
        String result1 = r1Value + r2Value;
        //System.out.println("Result of BR : " + result1);
        short result = Short.parseShort(String.valueOf(Integer.parseInt(result1, 2)));
        ;
        //System.out.println("Result in BR"+result);
        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return (short) (result - 1);
    }

    public byte SAL(Register R1, byte imm) {
        byte result = (byte) (R1.getValue() << imm);
        if (result == 0) {
            status_register.setZeroFlag(1);
        }
        else {
            status_register.setZeroFlag(0);
        }
        if (result < 0) {
            status_register.setNegativeFlag(1);
        }
        else {
            status_register.setNegativeFlag(0);
        }
        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return result;
    }

    public byte SAR(Register R1, byte imm) {
        byte result = (byte) (R1.getValue() >> imm);
        //System.out.println("Immeditae value to shift with:  " + imm);
        //System.out.println("Register value : " + R1.getValue());
        if (result == 0) {
            status_register.setZeroFlag(1);
        }
        else {
            status_register.setZeroFlag(0);
        }
        if (result < 0) {
            status_register.setNegativeFlag(1);
        }
        else {
            status_register.setNegativeFlag(0);
        }
        System.out.println("The carry flag is "+ status_register.carryFlag);
        System.out.println("The overflowFlag flag is "+ status_register.overflowFlag);
        System.out.println("The negativeFlag flag is "+ status_register.negativeFlag);
        System.out.println("The signFlag flag is "+ status_register.signFlag);
        System.out.println("The zeroFlag flag is "+ status_register.zeroFlag);
        return result;
    }

    public byte LDR(Register R1, short address) {
        if (address < 2048)
            return data_memory.readData(address);

        return -1;
    }

    public void STR(Register R1, short address) {
        data_memory.writeData(address, R1.getValue());

        System.out.println("printing memory at address " + address + " value in memory is " + data_memory.dataMemory[address]);
    }

}

