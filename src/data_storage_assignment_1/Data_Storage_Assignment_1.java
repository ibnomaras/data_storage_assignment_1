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
                System.out.print((j+1) + ")");
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
            System.out.println("\nRun Name = " + RunsFilesNames[i] + " and It's lenght is: " + len);
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
        
        for (int i = 0 ; i < SortedRunsNames.length ; i += K)
        {
            int id_1,id_2,idx_1 = 0,idx_2 = 0;
            String temp_run = "temp_run_" + i;
            RandomAccessFile temp_rn = new RandomAccessFile(temp_run, "rw");
            String to_be_merged [] = new String[K];
            boolean flag = true;
            System.out.println("\nSort merging files: ");
            for(int j = 0 ; j < K ; j ++)
            {
                System.out.print( SortedRunsNames[i+j] +" ");
                to_be_merged[j]=SortedRunsNames[i+j];
            }
            
            RandomAccessFile file_1 = new RandomAccessFile(to_be_merged[0], "rw");
            RandomAccessFile file_2 = new RandomAccessFile(to_be_merged[1], "rw");
            
            file_1.seek(0);
            file_2.seek(0);
            id_1 = file_1.read();
            id_2 = file_2.readInt();
            while(flag == true)
            {
                //System.out.println("Comparing: id_1= " + id_1 + " id_2 = " + id_2);
                if (id_1 < id_2)
                {  
                    temp_rn.writeInt(id_1);
                    idx_1 = idx_1 + 4;
                    file_1.seek(idx_1);
                    id_1 = file_1.readInt();
                    temp_rn.writeInt(id_1);
                    if (idx_1 + 4 >= file_1.length())
                    {
                        for (int f = idx_2 ; f < file_2.length() ; f= f + 8)
                        {
                            file_2.seek(f-4);
                            id_1 = file_1.readInt();
                            temp_rn.writeInt(id_1);
                            file_2.seek(f+4);
                            id_2 = file_2.readInt();
                            temp_rn.writeInt(id_2);
                        }
                        
                        flag = false;
                    }
                    else
                    {
                        idx_1 = idx_1 + 4;
                        file_1.seek(idx_1);
                        id_1 = file_1.readInt(); 
                    }
                    
                }
                
                else
                {
                    temp_rn.writeInt(id_2);
                    idx_2 = idx_2 + 4;
                    file_2.seek(idx_2);
                    id_2 = file_2.readInt();
                    temp_rn.writeInt(id_2);
                    if (idx_2 + 4 >= file_2.length())
                    {
                        for (int f = idx_1 ; f < file_1.length() ; f =f+8)
                        {
                            file_1.seek(f);
                            id_1 = file_1.readInt();
                            temp_rn.writeInt(id_1);
                            file_1.seek(f+4);
                            id_1 = file_1.readInt();
                            temp_rn.writeInt(id_1);
                        }
                        
                        flag = false;
                    }
                    else
                    {
                        idx_2 = idx_2 + 4;
                        file_2.seek(idx_2);
                        id_2 = file_2.readInt(); 
                    }
                    
                }
            }
            
            
            System.out.println("\n");
            Displayrun(temp_run);
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
            
    
    static void BinarySearchFor (RandomAccessFile index,int id, int start , int end) throws FileNotFoundException, IOException
    {
        
        int mid = (start+end)/2, temp=0;
        index.seek(mid);
        temp = index.readInt();
        System.out.println("Mid is: " + mid +" & Temp is "+ temp + " Start is: " + start + " End is: " + end);
        if (temp == id)
        {
            index.seek(mid+4);
            System.out.println("ID: " + id + " Was found. And it's associtated Byteoffset is: " + index.readInt());
        }
        
        else
        {
            
            if (mid > id)
            {
                BinarySearchFor (index,id, start , mid);
            }

            else if (mid < id)
            {
                BinarySearchFor (index,id, mid , end);
            }
        
        }
        
    }
            
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        System.out.print("Assignment #1 \n");
        
        int num_of_runs ,num_of_ks = 1, search_id ;
        String file_name = "Index.bin";
        String Sortedfilename = "Sorted.bin";
        Scanner reader = new Scanner(System.in);
        String [] runs , sorted_runs ;
        
        System.out.print("Enter Number of runs: ");
        num_of_runs = reader.nextInt();
        runs = DivideInputFileIntoRuns(file_name,num_of_runs);
        for (int i = 0 ; i < runs.length ; i++) System.out.println(runs[i]);       
        sorted_runs = SortEachRunOnMemoryAndWriteItBack(runs);
        
        System.out.print("\nEnter Number of Ks: ");
        num_of_ks = reader.nextInt();
        DoKWayMergeAndWriteASortedFile(sorted_runs,num_of_ks,Sortedfilename);
        
        System.out.print("\nEnter ID to search for: ");
        search_id = reader.nextInt();
        RandomAccessFile sorted = new RandomAccessFile("temp_run_0", "rw");
        BinarySearchFor(sorted,search_id,0 , (int) sorted.length());
        
        
        System.out.println("\nDelete the runs? (1/0) ");
        if(reader.nextInt()== 1)
        {
            // Deleteing runs
            File file = new File ("temp_run_0");
            file.delete();
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
