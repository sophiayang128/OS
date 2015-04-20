import java.io.*;
import java.util.ArrayList;

public class SimpleShellN
{
    public static void main(String[] args) throws IOException
    {
        String commandLine;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String MyPath=System.getProperty("user.dir");  //get the current path
        String HomePath=MyPath;
        ArrayList<String> commandHistory=new ArrayList<String>();
        int tem=0;
        // we break out with <control><C>
        
        while (true)
        {
            // read what the user entered
            System.out.print(MyPath+"@");
            System.out.print("jsh>");
            commandLine = console.readLine();
            // if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;
            /* The steps are:
             (1) parse the input to obtain the command and any parameters
             (2) create a ProcessBuilder object
             (3) start the process
             (4) obtain the output stream
             (5) output the contents returned by the command */
            //public ProcessBuilder (List<String> command);
            if(commandLine.charAt(0)=='!')
            {
                tem=0;
                if(commandLine.length()==2&&commandLine.charAt(1)=='*')
                {
                    //commandHistory.remove(commandHistory.size()-1);
                    for(int i=0;i<commandHistory.size();i++)
                    {
                        String a=commandHistory.get(i);
                        System.out.print(i+" "+a+"\n");
                        
                    }
                    //commandHistory.add("!*");
                    continue;
                }
                if(commandLine.length()==2&&commandLine.charAt(1)=='!')
                {
                    tem=1;
                }
                else
                {
                    for(int i=1;i<commandLine.length();i++)
                    {
                        tem=tem*10+commandLine.charAt(i)-'0';
                    }
                }
                if (tem>commandHistory.size())
                {
                    System.out.print(tem);
                    System.out.print("error:event not found! \n");
                    System.out.print(commandHistory.size());
                    continue;
                }
                else
                {
                    String exCommand=commandHistory.get(commandHistory.size()-tem);
                    System.out.print(exCommand+"\n");
                    commandLine=exCommand;
                }
            }
            /*String lastCommand=commandHistory.get(commandHistory.size()-2);
             System.out.println(lastCommand);
             String exCommand;
             if(lastCommand=="!*")
             {
             exCommand=commandHistory.get(commandHistory.size()-tem-2);
             }
             else
             {
             exCommand=commandHistory.get(commandHistory.size()-tem-1);}
             System.out.print(exCommand+"\n");
             commandLine=exCommand;
             }
            
            String exCommand=commandHistory.get(commandHistory.size()-tem);
            System.out.print(exCommand+"\n");
            commandLine=exCommand;
            }
            else
            {*/
            String[] lineSplit = commandLine.split(" ");
            if(lineSplit[0].equalsIgnoreCase("cat"))
            {
                commandHistory.add(commandLine);
            }
            if(lineSplit[0].equalsIgnoreCase("cd"))
            {
                commandHistory.add(commandLine);
                if(lineSplit.length==1)
                {
                    MyPath=HomePath;
                }
                else if (lineSplit[1].charAt(0) == '/')
                {
                    String[] currentSplit = lineSplit[1].split("/");
                    String currentPath = "/";
                    boolean currentFlag=true;
                    for (int i = 1; i < currentSplit.length; i++)
                    {
                        ProcessBuilder pb = new ProcessBuilder("ls");
                        File filedir = new File(currentPath);
                        pb.directory(filedir);
                        Process process = pb.start();
                        InputStream is = process.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        boolean flag = false;
                        while ((line = br.readLine()) != null)
                        {
                            if (currentSplit[i].equalsIgnoreCase(line))
                            {
                                flag = true;
                                break;
                            }
                        }
                        br.close();
                        if (flag)
                        {
                            currentPath=currentPath+ "/" +currentSplit[i];
                            continue;
                        }
                        else
                        {
                            System.out.print("error:Files cannot be found!\n");
                            currentFlag=false;
                            break;
                        }
                    }
                    if(currentFlag)
                    {
                        MyPath = lineSplit[1];
                    }
                }
                else
                {
                    String[] currentSplit = lineSplit[1].split("/");
                    String currentPath =MyPath;
                    boolean currentFlag=true;
                    for (int i = 0; i < currentSplit.length; i++)
                    {
                        ProcessBuilder pb = new ProcessBuilder("ls");
                        File filedir = new File(currentPath);
                        pb.directory(filedir);
                        Process process = pb.start();
                        InputStream is = process.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        // read what is returned by the command
                        String line;
                        boolean flag = false;
                        while ((line = br.readLine()) != null) 
                        {
                            if (currentSplit[i].equalsIgnoreCase(line)) 
                            {
                                flag = true;
                                break;
                            }
                        }
                        br.close();
                        if (flag)
                        {
                            currentPath=currentPath+ "/" + currentSplit[i];
                            continue;
                        }	 
                        else 
                        {
                            System.out.print("error:Files cannot be found!\n");
                            currentFlag=false;
                            break;
                        }
                    }
                    if(currentFlag)  
                    {
                        MyPath = MyPath + "/" + lineSplit[1];
                    }
                }
            }
            else if(lineSplit.length==1&&lineSplit[0].equalsIgnoreCase("quit"))
            {
                System.out.println("Goodbye");
                commandHistory.clear();
                System.exit(0);
            }
            else
            {
                try
                {
                    commandHistory.add(commandLine);
                    ProcessBuilder pb = new ProcessBuilder(lineSplit);
                    File filedir=new File(MyPath);
                    pb.directory(filedir);
                    Process process = pb.start();
                    // obtain the input stream
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    // read the output of the process
                    String line;
                    while ( (line = br.readLine()) != null)
                        System.out.println(line);
                    br.close();
                }
                catch(Exception e)
                {
                    commandLine=null;
                    System.out.println(e.getMessage());
                }
            }
        }
        
    }
}