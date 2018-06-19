package wanglei;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


/**
 * TODO 添加类的描述
 *
 * @author zl_wanglei
 * @version C10 2018年6月19日
 * @since SDP V300R003C10
 */
public class Main
{
    /**
     * 原始文件
     */
    private static final File SOURCE_FILE = new File("E:/", "sourceFile.txt");
    
    /**
     * 排序后的文件
     */
    private static final File RESULT_FILE = new File("E:/", "resultFile.txt");
    
    /**
     * 字符串编码
     */
    private static final String CHARSETNAME = "UTF-8";
    
    /**
     * 排序前的数组
     */
    private static long[] ARRAY = null;
    

    
    /**
     * 文件行数
     * 
     * @return lines
     */
    private static int getFileLines()
    {
        long beginTime = System.currentTimeMillis();
        
        int index = 0;
        try 
        {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_FILE), CHARSETNAME));
            String line;
            while ((line = br.readLine()) != null)
            {
                index++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("read file lines times = %s", (endTime - beginTime)));
        
        return index;
    }
    
    /**
     * 读取文件
     */
    private static void readFile()
    {
        long beginTime = System.currentTimeMillis();
        
        int index = 0;
        BufferedReader br = null;
        try 
        {
            br =
                new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_FILE), CHARSETNAME));
            String line;
            while ((line = br.readLine()) != null)
            {
                ARRAY[index++] = Long.valueOf(line);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally{
            try
            {
                br.close();
            }
            catch (IOException e)
            {
                System.err.println("输入流关闭异常");
                e.printStackTrace();
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("read file times = %s", (endTime - beginTime)));
    }
    
    /**
     * 写入新文件
     */
    private static void writeFile()
    {
        BufferedWriter bw = null;
        try 
        {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(RESULT_FILE), CHARSETNAME));
            for (int i = 0; i < ARRAY.length; i++)
            {
                if (ARRAY[i] == 0)
                {
                    continue;
                }
                
                bw.write(String.valueOf(ARRAY[i]));
                bw.newLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally{
            try
            {
                bw.close();
            }
            catch (IOException e)
            {
                System.err.println("输出流关闭异常");
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * 多线程排序
     */
    private static void forkJoinSort()
    {
        long beginTime = System.currentTimeMillis();
        
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(new SortThread(ARRAY, 0, ARRAY.length - 1));
        forkJoinPool.shutdown();
        try
        {
            forkJoinPool.awaitTermination(10000, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("sort file times = %s", (endTime - beginTime)));
    }
    
    /**
     * TODO 添加方法注释
     *
     * @author zl_wanglei
     * @param args
     */
    public static void main(String[] args)
    {

        
        // 获取文件行数
        int lines = getFileLines();
        
        // 设置排序前和排序后的数组大小
        ARRAY = new long[lines];
        
        // 读取文件写入到BEFORE_ARRAY中
        readFile();
        
        // 利用forkjoin排序
        forkJoinSort();
        
        // 将排序后的结果写入到新文件
        writeFile();
        System.out.println(" it's over!");
    }
    
}
