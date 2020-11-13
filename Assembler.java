


package Assembler;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.util.ArrayList;



/**
 *
 * PROJECT NAME --- DUMMY ASSEMBLER
 * <br>
 * DEVELOPER --- ANIKET
 * <br>
 * COURSE -- BSC ( HONS ) COMPUTER SCIENCE LAST YEAR
 * <br>
 * COLLEGE --- DYAL SINGH COLLEGE ( DELHI UNIVERSITY )
 * <br>
 * COLLEGE ROLL-NUMBER -- 18/94026
 *<br>
 *     <br>
 *  FOR THIS ASSEMBLER :
 *  <br>
 *  LANGUAGE RULES -:
 *  <br>
 *  1. IT IS WHITESPACE SENSITIVE
 *  <br>
 *  2. IT IS ALSO CASE SENSITIVE
 *  <br>
 *  3. FOR INSTRUCTIONS SET PREFER MOT.txt
 *  <br>
 *  4. IT SUPPORTS ERROR HANDLING LIKE LITERAL SIZE , SYMBOL NOT DEFINED , DESTINATION ILLEGAL , ILLEGAL INSTRUCTION ,   ALL TYPE SYNTAX ERROR
 *<br>
 *
 *
 *<br>
 *  INSTRUCTION -:
 *  <br>
 *  JDK MUST GREATER OR EQAUL TO  14.0.1
 *  <br>
 *  MUST DOWNLOAD REQUIRED CLASS FOR @NotNull ANNOTATION
 *<br>
 *     <br>
 *         <br>
 *  EMAIL -:
 *  <br>
 *  aniketranag123@gmail.com
 *  <br>
 * aniketranag1234@gmail.com
 *<br>
 *     <br>
 *
 */



public class Assembler {
    static ArrayList<ArrayList<String>> section_table = new ArrayList<>();
    static ArrayList<ArrayList<String>> symbol_table = new ArrayList<>();
    static String file = "";
    static int location_counter = 0;
    static String lines[];
    static  FileOutputStream outputStream;
    static  final String output_file_name="/root/IdeaProjects/src/output.out";

    public static void main(String[] args) throws Exception {
        FileReader reader = new FileReader("/root/IdeaProjects/src/sourcecode.asm");
        for (; ; ) {
            int c = reader.read();
            if (c != -1)
                file += (char) c;
            else
                break;
        }
        lines = file.split("\n");
        firstPass();



        if (section_table.size() >= 1)
            section_table.get(section_table.size() - 1).add(1, location_counter + "");// TO ADD THE SIZE ENTRY OF LAST SECTION USED IN ASSEMBLY PROGRAM
        System.out.println("First Pass Successfully Done ");
        System.out.println("-------------SECTION TABLE ------------");
        System.out.println("Name\t Size");
        for (ArrayList<String> list : section_table)
            System.out.println(list.get(0) + "\t\t\t" + list.get(1));
        System.out.println("-------------SYMBOL TABLE ------------");
        System.out.println("Name\tSize\tType\tOffset");
        for (ArrayList<String> list : symbol_table)
            System.out.println(list.get(0) + "\t\t" + list.get(1) + "\t\t" + list.get(2) + "\t\t" + list.get(3));
        outputStream=new FileOutputStream(output_file_name);
        ArrayList<String> A =new ArrayList<>();
        A.add(0,"A");
        A.add(1,"DD");
        A.add(2,"REGISTER");
        A.add(3,11+"");

        ArrayList<String> B =new ArrayList<>();
        B.add(0,"B");
        B.add(1,"DD");
        B.add(2,"REGISTER");
        B.add(3,12+"");



        ArrayList<String> C =new ArrayList<>();
        C.add(0,"C");
        C.add(1,"DD");
        C.add(2,"REGISTER");
        C.add(3,13+"");


        ArrayList<String> D =new ArrayList<>();
        D.add(0,"D");
        D.add(1,"DD");
        D.add(2,"REGISTER");
        D.add(3,14+"");

        symbol_table.add(A);
        symbol_table.add(B);
        symbol_table.add(C);
        symbol_table.add(D);
        secondPass();
        System.out.println("Second Pass Successfully Done ..  Output File "+output_file_name+" is generated ");

    }

    static int value(@NotNull String temp) {
        if (temp.equals("DD"))
            return 4;
        else if (temp.equals("DW"))
            return 2;
        else
            return 1;

    }

    static void firstPass() {
        for (int i = 0; i < lines.length; i++) {
            String current = lines[i];
            current = current.trim();
            String words[] = current.split(" ");
            if (words.length == 0)
                continue;
            if (words[0].charAt(0) == '.') {
                if (words[0].equals(".CODE")) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(0, "CODE");
                    temp.add(1, "0");
                    if (section_table.size() >= 1)
                        (section_table.get(section_table.size() - 1)).add(1, "" + location_counter);
                    section_table.add(temp);
                    location_counter = 0;
                } else if (words[0].equals(".DATA")) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(0, "DATA");
                    temp.add(1, "0");
                    if (section_table.size() >= 1)
                        (section_table.get(section_table.size() - 1)).add(1, "" + location_counter);
                    section_table.add(temp);
                    location_counter = 0;
                } else {
                    System.out.println("There is a syntax error at line : " + (i + 1));
                    System.exit(100);
                }
            } else {
                if (words.length >= 2) {

                    if (words[1].equals("DD") || words[1].equals("DW") || words[1].equals("DB")) {

                        if (section_table.size() == 0) {
                            System.out.println("There is syntax error at line : " + (i + 1));
                            System.exit(100);
                        } else {
                            if (!section_table.get(section_table.size() - 1).get(0).equals("DATA")) // BECAUSE WE CAN ONLY DEFINE ANY ID. ONLY IN DATA SECTION
                                 {
                                System.out.println("There is syntax error at line : " + (i + 1));
                                System.exit(100);
                            }
                        }
                        if (words.length == 2)  // BCZ NO ANY INSTRUCTION EXIST WITH SIZE 2
                             {
                            System.out.println("There is syntax error at line : " + (i + 1));
                            System.exit(100);
                        } else {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(0, words[0]);
                            temp.add(1, words[1]);
                            temp.add(2, "Variable");
                            temp.add(3, location_counter + "");
                            symbol_table.add(temp);
                            location_counter = location_counter + (value(words[1]) * (words.length - 2));
                        }
                    } else {


                        switch (words[0]) {
                            case "MOV" -> {

                                if (words.length == 4) {
                                    if (!words[2].equals(",")) {
                                        System.out.println("There is some error at line : " + (i + 1));
                                        System.exit(100);
                                    } else {
                                        location_counter = location_counter + 9;
                                    }

                                } else {

                                    System.out.println("There is some error at line : " + (i + 1));
                                    System.exit(100);

                                }
                            }
                            case "ADD", "INC", "SUB" -> {

                                if (words.length == 2) {

                                    location_counter  += 5;
                                } else {
                                    System.out.println("There is some error at line : " + (i + 1));
                                    System.exit(100);
                                }

                            }
                            case "END" -> {

                                return;
                            }
                            case "CMP" -> {
                                if (words.length == 4) {
                                    if (words[2].charAt(0)!=',')
                                    {
                                        System.out.println("There is some error at line : " + (i + 1));
                                        System.exit(100);
                                    }
                                    location_counter += 9;
                                } else  {

                                    System.out.println("There is some error at line : " + (i + 1));
                                    System.exit(100);
                                }

                            }
                            default -> {
                                System.out.println("There is some error at line :" + (i + 1));
                                System.exit(100);
                            }
                        }


                    }
                }

            }


        }


    }
  static  boolean search(String name)
  {
      for(ArrayList<String> te:symbol_table)
      {
          if (te.get(0).equals(name))
              return true;
      }

      return false;
  }

  static  void writeNumber(String number,String size,OutputStream outputStream,int line_number) throws  Exception
  {

      int value = Integer.parseInt(number);
      String hexvalue = Integer.toHexString(value);
      if (hexvalue.length()>value(size)*2)
      {
          System.out.println("ERROR : NUMBER IS BIGGER , LINE # "+(line_number+1));
          deleteFile(output_file_name,outputStream);
          System.exit(100);
      }
      for (int i2 = hexvalue.length(); i2 < value(size) *2; i2++)
          hexvalue = "0" + hexvalue;
      outputStream.write(hexvalue.getBytes());



  }
  static  void deleteFile(String name, @NotNull OutputStream outputStream) throws Exception
  {
      outputStream.close();
      File file=new File(name);
      boolean isdeleted=file.delete();
      System.out.println(isdeleted?"File is deleted Successfully ":"ERROR : FILE IS NOT DELETED ...");
  }



  static  String getLocationCounter(String name)
  {
      String lc="NF";  //NF FOR NOT FOUND
   for(int i=0;i<symbol_table.size();i++)
   {
       if (symbol_table.get(i).get(0).equals(name))
       {
           lc= symbol_table.get(i).get(3);
       }
   }
   if (!lc.equals("NF"))
   {
       String hex=Integer.toHexString(Integer.parseInt(lc));
       for (int i=hex.length();i<8;i++)
           hex="0"+hex;
       return hex;
   }
   return lc;
  }


   static String getOpcode(String ins)
   {

       switch (ins)
       {

           case "ADD" -> {
               return "7B";
           }
           case "SUB" -> {
               return "8C";
           }
                   case "MOV" ->{
               return "8A";
                   }
                case "CMP" ->
                        {
                            return "5E";
                        }
                        case "INC" ->
                                {
                                    return "9D";
                                }


           default ->
                   {
                       return "NF"; //NF FOR NOT FOUND
                   }
       }


   }

    static void secondPass() throws  Exception {


        for(int i=0;i<lines.length;i++)
        {
            String current=lines[i];
            current=current.trim();
            String words[]=current.split(" ");
            if (words.length==0)
                continue;
            if (words[0].charAt(0)=='.')
            {
                if (words[0].equals(".CODE") || words[0].equals(".DATA"))
                {
                    location_counter=0;
                }
            }
            else
            {
                if (words.length>=2)
                {

                    if (words[1].equals("DD")||words[1].equals("DW")||words[1].equals("DB"))
                    {

                         writeNumber(""+location_counter,"DD",outputStream,i);
                           outputStream.write(' ');
                            for(int i1=2;i1<words.length;i1++)
                            {
                                try {
                                    writeNumber(words[i1],words[1],outputStream,i1);
                                }
                                catch (Exception exp)
                                {
                                    System.out.println("Error : DATA MUST BE LITERAL , LINE NUMBER "+(i+1));
                                    deleteFile(output_file_name,outputStream);
                                    System.exit(100);
                                }
                            }
                            outputStream.write('\n');
                            location_counter=location_counter+(value(words[1])*(words.length-2));
                    }
                    else {
                        switch (words[0]) {
                            case "MOV" -> {
                                writeNumber(""+location_counter,"DD",outputStream,i);
                                outputStream.write(' ');
                                outputStream.write(getOpcode("MOV").getBytes());
                                       if (!search(words[1]))
                                       {
                                           try{
                                               Integer.parseInt(words[1]);
                                               System.out.println("ERROR : Destination can never be literal , Line # "+(i+1));
                                               deleteFile(output_file_name,outputStream);
                                               System.exit(100);
                                           }
                                           catch (Exception ep)
                                           {
                                               System.out.println("ERROR : Symbol is not found  , Line # "+(i+1));
                                               deleteFile(output_file_name,outputStream);
                                               System.exit(100);
                                           }
                                       }
                                       else
                                       {
                                           outputStream.write(getLocationCounter(words[1]).getBytes());
                                       }
                                       if (!search(words[3]))
                                       {

                                           try
                                           {
                                               writeNumber(words[3],"DD",outputStream,i);
                                               outputStream.write('\n');
                                           }
                                           catch (Exception ep)
                                           {
                                               System.out.println("ERROR : SYMBOL IS NOT FOUND , LINE : "+(i+1));
                                               deleteFile(output_file_name,outputStream);
                                               System.exit(100);
                                           }
                                       }
                                       else
                                       {
                                           outputStream.write(getLocationCounter(words[3]).getBytes());
                                           outputStream.write('\n');
                                       }
                                            location_counter = location_counter + 9;

                            }
                            case "ADD", "INC", "SUB" -> {


                                  writeNumber(""+location_counter,"DD",outputStream,i);
                                  outputStream.write(' ');
                                  outputStream.write(getOpcode(words[0]).getBytes());
                                   try{
                                       writeNumber(words[1],"DD",outputStream,i);
                                       if (words[0].equals("INC"))
                                       {
                                           System.out.println("In INC instruction operand can never le literal at line : "+(i+1));
                                           deleteFile(output_file_name,outputStream);
                                           System.exit(100);
                                       }

                                   }
                                   catch (Exception ep)
                                   {
                                       if (!search(words[1]))
                                       {
                                           System.out.println("ERROR : Symbol is not found , Line # "+(i+1));
                                           deleteFile(output_file_name,outputStream);
                                           System.exit(100);
                                       }
                                       else
                                       {
                                           outputStream.write(getLocationCounter(words[1]).getBytes());
                                           outputStream.write('\n');
                                       }

                                   }
                                        location_counter += 5;

                                }
                            case "END" -> {
                                return;
                            }
                            case "CMP" -> {
                                writeNumber(""+location_counter,"DD",outputStream,i);
                                outputStream.write(' ');
                                outputStream.write(getOpcode("CMP").getBytes());
                                boolean first_second=true;
                                  try
                                  {
                                     int val1= Integer.parseInt(words[1]);
                                      String hex=Integer.toHexString(val1);
                                      for(int i1=hex.length();i1<8;i1++)
                                          hex="0"+hex;
                                      outputStream.write(hex.getBytes());
                                     first_second=false;

                                    int val2= Integer.parseInt(words[3]);
                                    String hex2=Integer.toHexString(val2);
                                    for(int i1=hex2.length();i1<8;i1++)
                                        hex2="0"+hex2;
                                    outputStream.write(hex2.getBytes());
                                    outputStream.write('\n');
                                  }
                                  catch (Exception e)
                                  {

                                      if (first_second) {
                                          if (!search(words[1])) {
                                              System.out.println("ERROR: SYMBOL IS NOT FOUND , LINE #" + (i + 1));
                                              deleteFile(output_file_name, outputStream);
                                              System.exit(100);
                                          }
                                          else
                                          {
                                              outputStream.write(getLocationCounter(words[1]).getBytes());
                                          }
                                          try
                                          {
                                              String hex2=Integer.toHexString(Integer.parseInt(words[2]));
                                              for(int i1=hex2.length();i1<8;i1++)
                                                  hex2="0"+hex2;
                                              outputStream.write(hex2.getBytes());
                                              outputStream.write('\n');
                                          }
                                          catch (Exception ep)
                                          {
                                              if (!search(words[3]))
                                              {
                                                  System.out.println("ERROR: SYMBOL IS NOT FOUND , LINE #" + (i + 1));
                                                  deleteFile(output_file_name, outputStream);
                                                  System.exit(100);
                                              }
                                              else
                                              {
                                                outputStream.write(getLocationCounter(words[3]).getBytes());
                                                outputStream.write('\n');
                                              }
                                          }
                                      }
                                      else
                                      {
                                          if (!search(words[3]))
                                          {
                                              System.out.println("ERROR: SYMBOL IS NOT FOUND , LINE #" + (i + 1));
                                              deleteFile(output_file_name, outputStream);
                                              System.exit(100);
                                          }
                                          else{
                                              outputStream.write(getLocationCounter(words[3]).getBytes());
                                              outputStream.write('\n');
                                          }
                                      }

                                  }
                                  location_counter += 9;
                            }
                        }

                    }
                }

            }


        }

    }
    }

