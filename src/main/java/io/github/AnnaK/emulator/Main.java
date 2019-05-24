package io.github.AnnaK.emulator;

import io.github.AnnaK.emulator.equipment.Impl.MultiprocessorImpl;
import io.github.AnnaK.emulator.equipment.Multiprocessor;
import io.github.AnnaK.emulator.equipment.Processor;

import java.util.List;
import java.util.Scanner;

//временная шняга для проверки логики работы
public class Main {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        /*final ApplicationConfig conf = new ApplicationConfig();
        conf.configure(Binder.class.newInstance());*/

        final Multiprocessor multiprocessor = new MultiprocessorImpl(4);

        final Scanner sc = new Scanner(System.in);
        while (true){
            final String in = sc.nextLine();
            control(multiprocessor.getProcessors(), in);
        }


//
//        List<Processor> processors = multiprocessor.getProcessors();
//        processors.get(0).writeToOwnCache("ex1");
//        processors.get(1).requestValidInfo(1);
//        processors.get(2).requestValidInfo(1);
//
//        processors.get(3).writeToOwnCache(1, "olo");

    }

    //команды в формате: wrt 1 1 аfuf  - команда процессор  id данные
    //rvi 1
    private static void control(final List<Processor> processors, String in){
        final Com com = parse(in);
        switch (com.getCommand()){
            case "wrt":
                wrt(processors.get(com.getProcNum()), com.getId(), com.getData());
                break;
            case "rvi":
                rvi(processors.get(com.getProcNum()), com.getId());
                break;
        }

    }

    private static void wrt(final Processor processor, final int id, final String data){
        processor.writeToOwnCache(id, data);
    }

    private static void rvi(final Processor processor, final int id){
        processor.requestValidInfo(id);
    }

    private static class Com{
        final int procNum;
        final String command;
        final String data;
        final int id;

        Com(final int procNum,
            final String command,
            final String data,
            final int id){
            this.procNum = procNum;
            this.command = command;
            this.id = id;
            this.data = data;
        }

        public int getProcNum() {
            return procNum;
        }

        public String getCommand() {
            return command;
        }

        public String getData() {
            return data;
        }

        public int getId() {
            return id;
        }
    }

    private static Com parse (String in){
        final String com = in.substring(0, 3);
        in = in.substring(4);
        final int proc = Integer.parseInt(String.valueOf(in.charAt(0)));
        in = in.substring(2);
        final int id = Integer.parseInt(String.valueOf(in.charAt(0)));
        if(in.length() > 2 ){
            in = in.substring(2);
        }
        return new Com(proc, com, in, id);
    }
}
