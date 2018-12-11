import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        String filePath = "C:\\Program Files (x86)\\Steam\\userdata\\180093392\\219740\\remote";
        String destPath = "D:\\video";
        Tool tool = new Tool();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String pattern = "survival_[1-4]";
        Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        while(true){
            WatchKey key = watchService.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            for (WatchEvent<?> event : watchEvents) {
                String document = filePath + File.separator + event.context().toString();
                if(StandardWatchEventKinds.ENTRY_CREATE == event.kind()){
                    System.out.println(tool.getCurrent()+":创建：[" + document+ "]");
                    System.out.println("匹配结果："+event.context().toString().matches(pattern));
                    if(event.context().toString().matches(pattern)||event.context().toString().equals("saveindex")){
                        try {
                            File source = new File(document);
                            File dest = new File(destPath+ File.separator + dateFormat.format(tool.getCurrent())+ event.context().toString());
                            Files.copy(source.toPath(),dest.toPath());
                            System.out.println(dateFormat.format(tool.getCurrent()) + " "+ dest.toPath()+"备份完成");
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                }
                if(StandardWatchEventKinds.ENTRY_MODIFY == event.kind()){
                    System.out.println(tool.getCurrent()+":修改：[" + document + "]");
                    System.out.println("匹配结果"+event.context().toString().matches(pattern));
                }
                if(StandardWatchEventKinds.ENTRY_DELETE == event.kind()){
                    System.out.println(tool.getCurrent()+":删除：[" + document + "]");
                }
            }
            key.reset();
        }
    }
    public static class Tool{
        private Date current;
        private Date getCurrent(){
            current = new Date();
            return current;
        }
    }
    public static void  backupfile(){
         //TODO
    }
}
