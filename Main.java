import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        int total_words_count = 0;
        String[] files = {"C:\\Users\\pc\\Downloads\\Text Files\\file1.txt", "C:\\Users\\pc\\Downloads\\Text Files\\file2.txt", "C:\\Users\\pc\\Downloads\\Text Files\\file3.txt", "C:\\Users\\pc\\Downloads\\Text Files\\file4.txt", "C:\\Users\\pc\\Downloads\\Text Files\\file5.txt"};
        String[] files_content = read_files(files);
        String word = "";
        Scanner input = new Scanner(System.in);
        System.out.println("What is the Word that you want to search for:");
        word = input.nextLine();


        ExecutorService executor = Executors.newFixedThreadPool(files.length);
        List<Future<Integer>> futures = new ArrayList<>();
        for(String file_content : files_content){
            String finalWord = word;
            Future future = executor.submit(() -> search_for_word(file_content, finalWord));
            futures.add(future);
        }
        int thread_counter = 0;
        for (Future<Integer> future : futures) {
            thread_counter++;
            int result = future.get();
            total_words_count += result;
            System.out.println("Thread " + thread_counter +" found the word: " + word + " in file" + thread_counter +" " + result + " time(s)");
        }

        System.out.println("In total you word was found " + total_words_count + " times(s)");

    }


    public static String[] read_files(String[] files) throws IOException {
        int num = files.length;
        String file_name;
        String[] files_content = new String[num];
        for(int i = 0; i < files.length; i++){
            StringBuilder content = new StringBuilder();
            BufferedReader reader = null;
            file_name = files[i];

            reader = new BufferedReader(new FileReader(file_name));
            String line;
            while((line = reader.readLine())!= null){
                content = content.append(line).append(System.lineSeparator());
            }
            files_content[i] = content.toString();
        }
        return files_content;
    }

    public static int search_for_word(String text, String key){
        int counter = 0;
        String[] words = text.split(" ");
        for(int i = 0; i< words.length; i++){
            if(words[i].equals(key)){
                counter++;
            }
        }
        return counter;
    }
}