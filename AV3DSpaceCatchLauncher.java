/*
 * Proprietário: Antonio Vandré Pedrosa Furtunato Gomes.
 * 
 * Launcher do Game AVSpaceCatch.
 * 
 * Dependências: AntonioVandre.
 * 
 * Sugestões ou comunicar erros: "a.vandre.g@gmail.com".
 * 
 * Licença de uso: Atribuição-NãoComercial-CompartilhaIgual (CC BY-NC-SA).
 * 
 * Última atualização: 21-02-2023.
 */

import java.io.*;
import java.net.URL;

import java.lang.ProcessBuilder;

public class AV3DSpaceCatchLauncher
    {
    public static String VersaoLauncher = "21-02-2023";

    public static String URLAVBlockVersao = "https://github.com/antoniovandre2/AV3DSpaceCatch/raw/main/AV3DSpaceCatchVersao.txt";

    public static String ArquivoAVBlockVersao = "AV3DSpaceCatchVersao.txt";

    public static String URLAV3DSpaceCatch = "https://github.com/antoniovandre2/AV3DSpaceCatch/raw/main/AV3DSpaceCatch.jar";

    public static String ArquivoAV3DSpaceCatch = "AV3DSpaceCatch.jar";

    public static String MensagemErroAtualizar = "Erro ao atualizar o game AV3DSpaceCatch.";

    public static String MensagemErroExecutar = "Erro ao executar o game AV3DSpaceCatch.";

    public static void main(String[] args)
        {
        int FlagSucessoDownloadNet = 1;

        try
            {
            downloadUsingStream(URLAVBlockVersao, ArquivoAVBlockVersao + ".tmp");
            } catch (IOException e) {FlagSucessoDownloadNet = 0;}

        if (FlagSucessoDownloadNet == 1)
            {
            File file = new File(ArquivoAVBlockVersao);
            int FlagSucessoVersaoLocal = 1;
            String VersaoLocal = "";

            try
                {
                BufferedReader br = new BufferedReader(new FileReader(file));
                VersaoLocal = br.readLine();
                } catch (IOException e) {FlagSucessoVersaoLocal = 0;}

            File fileNet = new File(ArquivoAVBlockVersao + ".tmp");
            int FlagSucessoVersaoNet = 1;
            String VersaoNet = "";

            try
                {
                BufferedReader brNet = new BufferedReader(new FileReader(fileNet));
                VersaoNet = brNet.readLine();
                } catch (IOException e) {FlagSucessoVersaoNet = 0;}

            if ((FlagSucessoVersaoLocal == 1) && (FlagSucessoVersaoNet == 1))
                {
                if (! (VersaoNet.equals(VersaoLocal)))
                    try
                        {
                        downloadUsingStream(URLAV3DSpaceCatch, ArquivoAV3DSpaceCatch);
                        downloadUsingStream(URLAVBlockVersao, ArquivoAVBlockVersao);
                        } catch (IOException e) {}
                }
            else
                try
                    {
                    downloadUsingStream(URLAV3DSpaceCatch, ArquivoAV3DSpaceCatch);
                    downloadUsingStream(URLAVBlockVersao, ArquivoAVBlockVersao);
                    } catch (IOException e) {}
            }

        try
            {
            String ArgumentoEstatisticas = "";

            if (args.length != 0) ArgumentoEstatisticas = args[0];

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", ArquivoAV3DSpaceCatch, ArgumentoEstatisticas);
            Process p = pb.start();
            } catch (IOException e) {System.out.println(MensagemErroExecutar);}
        }

    private static void downloadUsingStream(String urlStr, String file) throws IOException
        {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
        }
}
