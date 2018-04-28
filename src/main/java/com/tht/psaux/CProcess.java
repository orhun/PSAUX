package com.tht.psaux;
public class CProcess {
    private String USER, WCHAN, PC, NAME, PPID, USERSALT, CPU;
    private int PID, VSIZE, RSS, PPIDSALT;

    public CProcess(String USER, int PID, String PPID, int VSIZE, int RSS, String CPU, String WCHAN, String PC, String NAME, String USERSALT, int PPIDSALT){
        this.USER = USER;
        this.PID = PID;
        this.PPID = PPID;
        this.VSIZE = VSIZE;
        this.RSS = RSS;
        this.WCHAN = WCHAN;
        this.PC = PC;
        this.NAME = NAME;
        this.USERSALT = USERSALT;
        this.PPIDSALT = PPIDSALT;
        this.CPU = CPU;
    }
    public String getUser(){
        return USER;
    }
    public int getPid(){
        return PID;
    }
    public String getPPID(){return PPID;}
    public int getVSIZE(){return VSIZE;}
    public int getRSS(){return RSS;}
    public String getWCHAN(){return WCHAN;}
    public String getPC(){return PC;}
    public String getPName(){
        return NAME;
    }
    public String getUSERSALT(){return USERSALT;}
    public int getPPIDSALT(){return PPIDSALT;}
    public String getCPU(){return CPU;}
}
