package TestDemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;

import java.io.IOException;
import java.net.URI;

public class TestHadoopFile {
    private Configuration conf = new Configuration();
    private String user = "root";

    public static void main(String[] args) throws IOException, InterruptedException {
        TestHadoopFile testHadoopFile = new TestHadoopFile();
        testHadoopFile.fileSystemConfiguration();
    }

    public void fileSystemConfiguration() throws IOException, InterruptedException {
        // 获取配置文件对象
        //配置hdfs的路径
        conf.set("fs.defaultFS", "hdfs://10.19.154.149:9000");
        String filePath = "/user/root/demo.txt";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 100001; i++) {
            sb.append("hadoop demo " + i + "\r\n");
        }
        FileSystem fsSource = FileSystem.get(URI.create("hdfs://10.19.154.149:9000"), conf, user);
        FSDataOutputStream fileWrite = fsSource.create(new Path("/input/test.txt"));
        fileWrite.write(sb.toString().getBytes());
        //创建文件件
        //fsSource.mkdirs();
        //创建文件
        // FSDataOutputStream fsdos = fsSource.create(new Path("/input/test.txt"));
        // fsdos.write(sb.toString().getBytes());
        try {
            RemoteIterator<LocatedFileStatus> iter = fsSource.listFiles(new Path("/"), true);
            //这里的第二个参数true表示递归遍历，false反之
            while (iter.hasNext()) {
                LocatedFileStatus file = iter.next();
                String Path_file = file.getPath().toString();
                // 获取文件目录
                System.out.println(user + "$:" + Path_file.substring(21));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WriteSequenceFile() throws IOException, InterruptedException {
        conf.set("fs.defaultFS", "hdfs://10.19.154.149:9000");
        String filePath = "/user/root/demo.txt";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 100001; i++) {
            sb.append("hadoop demo " + i + "\r\n");
        }
        FileSystem fsSource = FileSystem.get(URI.create("hdfs://10.19.154.149:9000"), conf, user);
        FSDataOutputStream fileWrite = fsSource.create(new Path("/input/test.txt"));
        fileWrite.write(sb.toString().getBytes());
        SequenceFile.Writer.Option keyClassOption = SequenceFile.Writer.keyClass(IntWritable.class);
        SequenceFile.Writer.Option valueClassOption = SequenceFile.Writer.valueClass(IntWritable.class);
        //创建一个sequenceFile 的写 文件段落
        SequenceFile.Writer writer = SequenceFile.createWriter(conf, keyClassOption, valueClassOption);
    }

}
