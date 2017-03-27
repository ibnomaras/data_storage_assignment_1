/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_storage_assignment_1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile ;
import java.util.Arrays;
import java.util.Scanner;



/**
 *
 * @author abdelrhman
 * 
 * ID_1 : 20110221 ## Name_1: Abd El-Rahman Omar Youssif
 * ID_2 : 20110224 ## Name_2 : Abd El-Rahman Mohammed Hussien
 * 
 */


public class Data_Storage_Assignment_1 {

    /**
     * @param args the command line arguments
     */
    static String[] DivideInputFileIntoRuns (String Inputfilename, int runSize) throws IOException
    {
        String[] runs = new String[runSize] ;
        int num_of_records , records_per_run ;
        RandomAccessFile index_file = new RandomAccessFile(Inputfilename, "rw");
        //index_file.seek(0);
        num_of_records =  (int) (index_file.length()/8);
        System.out.println("Num of records in the file: " + num_of_records );
        records_per_run = num_of_records/runSize;
        System.out.println("Num of records in each file: " + records_per_run );
        
        for (int i = 1 ; i <= runSize ; i++)
        {
            String run_name = "runs/Run_Number_";
            run_name += i ;
            run_name += ".bin" ;
            runs[i-1] = run_name;
            RandomAccessFile run = new RandomAccessFile(run_name, "rw");
            index_file.seek((i-1)*8*records_per_run);
            for (int j =0 ; j < (8*records_per_run) ; j++) run.write(index_file.read());     
        }
        
        return runs ;  
    }
    static void Displayrun (String RunName) throws FileNotFoundException, IOException
    {
        RandomAccessFile res = new RandomAccessFile(RunName, "rw");
            
            for (int j = 0 ; j < res.length()/8 ; j++)
            {
                res.seek(j*8);
                System.out.print("ID : " + res.readInt());
                res.seek((j*8)+4);
                System.out.println(" With offset: " + res.readInt());
            }
    }
    static String []  SortEachRunOnMemoryAndWriteItBack (String [] RunsFilesNames) throws FileNotFoundException, IOException
    {
        String[] sortedruns = new String[RunsFilesNames.length] ;
        
        for (int i = 0 ; i < RunsFilesNames.length ; i ++)
        {
            //Start sorting The Runs one by one
            RandomAccessFile current_run = new RandomAccessFile(RunsFilesNames[i], "rw");
            int records[][];
            int keys[];
            int len = (int) current_run.length();
            records = new int[len/8][2];
            keys = new int[len/8];
            System.out.println("\n Run Name = " + RunsFilesNames[i] + " and It's lenght is: " + len);
            String run_name = "sorted_runs/Run_Number_";
            run_name += i+1 ;
            run_name += ".bin" ;
            for (int j = 0 ; j < len/8 ; j++)
            {
                // Putting keys in The Keys array and Both Key and offsit in records array
                current_run.seek(j*8);
                //System.out.print("ID : " + current_run.readInt());
                keys[j]= records[j][0] = current_run.readInt();
                current_run.seek((j*8)+4);
                //System.out.println(" With offset: " + current_run.readInt());
                records[j][1] = current_run.readInt();
            }
            Arrays.sort(keys);
            
            for (int j = 0 ; j < keys.length ; j++)
            {
                for (int k = 0 ; k < records.length ; k++)
                {
                    if(records[k][0]==keys[j])
                    {
                        // Map the sorted Keys array with the records array and write the result into
                        // the run'sfile
                        
                        RandomAccessFile sorted_run = new RandomAccessFile(run_name, "rw");
                        sortedruns [i] = run_name;
                        sorted_run.seek(sorted_run.length());
                        sorted_run.writeInt(records[k][0]);
                        sorted_run.seek(sorted_run.length());
                        sorted_run.writeInt(records[k][1]);
                    }
                }
            }
            Displayrun(run_name);
        }
        
        
        return sortedruns;
    }
     
    static void DoKWayMergeAndWriteASortedFile(String [] SortedRunsNames, int K ,String Sortedfilename) throws FileNotFoundException, IOException
    {
        RandomAccessFile result = new RandomAccessFile(Sortedfilename, "rw");
        
        if (K == SortedRunsNames.length)
        {
            for (int i = 0 ; i < SortedRunsNames.length ; i +=2)
            {
                
            
            }
        }
        
        
        /*
            RandomAccessFile res = new RandomAccessFile(result, "rw");
            System.out.println("Result File Results: ");
            for (int j = 0 ; j < 64 ; j++)
            {
                res.seek(j*8);
                System.out.print("ID : " + res.readInt());
                res.seek((j*8)+4);
                System.out.println(" With offset: " + res.readInt());
            }
        */
    }
            
            
            
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        System.out.print("Assignment #1 \n");
        
        int num_of_runs ,num_of_ks = 1 ;
        String file_name = "Index.bin";
        String Sortedfilename = "Sorted.bin";
        Scanner reader = new Scanner(System.in);
        String [] runs , sorted_runs ;
        
        System.out.print("Enter Number of runs: ");
        num_of_runs = reader.nextInt();
        runs = DivideInputFileIntoRuns(file_name,num_of_runs);
        for (int i = 0 ; i < runs.length ; i++) System.out.println(runs[i]);       
        sorted_runs = SortEachRunOnMemoryAndWriteItBack(runs);
        
        System.out.print("\n Enter Number of Ks: ");
        num_of_ks = reader.nextInt();
        DoKWayMergeAndWriteASortedFile(sorted_runs,num_of_ks,Sortedfilename);
        
        System.out.println("Delete the runs? (1/0) ");
        if(reader.nextInt()== 1)
        {
            // Deleteing runs
            for (int i = 0 ; i < runs.length ; i++)
            {
                File file1 = new File(runs[i]);
                File file2 = new File(sorted_runs[i]);
                file1.delete();
                file2.delete();
            }
        }
    }
    
}
