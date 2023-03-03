/*
 * Proprietário: Antonio Vandré Pedrosa Furtunato Gomes (bit.ly/antoniovandre_legadoontologico).
 * 
 * Game AV3DSpaceCatch.
 * 
 * Dependências: AntonioVandre.
 * 
 * Sugestões ou comunicar erros: "a.vandre.g@gmail.com".
 * 
 * Licença de uso: Atribuição-NãoComercial-CompartilhaIgual (CC BY-NC-SA).
 * 
 * Última atualização: 03-03-2023
 */

import java.awt.*;

import java.util.LinkedList;

import javax.swing.*;
import javax.swing.JFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.lang.Thread;

import java.lang.Math;
import java.io.*;

import javax.sound.sampled.*;

import AntonioVandre.*;

public class AV3DSpaceCatch extends JComponent
    {
    public static String ArquivoAV3DSpaceCatchVersao = "AV3DSpaceCatchVersao.txt";

    // Variáveis globais.

    public int TamanhoPlanoX = 400; // Default: 400.
    public int TamanhoPlanoY = 400; // Default: 400.
    public static int MinTamanhoPlanoX = 300; // Default: 300.
    public static int MinTamanhoPlanoY = 300; // Default: 300.
    public double Velocidade = 50; // Default: 50.
    public double LimiteSuperiorVelocidade = 100; // Default: 100.
    public double LimiteInferiorVelocidade = 10; // Default: 80.
    public static String AV3DSpaceCatchIconFilePath = "AV3DSpaceCatch - Logo - 200p.png";
    public static int TamanhoEspacoLabelStatus = 280; // Default: 280.
    public static int TamanhoFonteLabelStatus = 11; // Default: 11.
    public double DistanciaTela = 2; // Default: valor inicial: 2.
    public static double DeslocamentoLinear = 1; // Default: 1.
    public static double DeslocamentoAngular = 0.1; // Default: 0.1.
    public int TamanhoAlvo = 10; // Default: 10.
    public double LimiteXAlvo = 200; // Default: 200.
    public double LimiteYAlvo = 200; // Default: 200.
    public double LimiteZAlvo = 100; // Default: 100.
    public double DistanciaCapturaAlvo = 20; // Default: 30.
    public Color CorAlvo = Color.WHITE;
    public Color CorGuias = Color.GREEN;
    public double FatorTonalidadeAproximacao = 30; // Default: 30.
    public String ArquivoSomCatch = "ES_PREL Hit Laser 4 - SFX Producer.wav";
    public String ArquivoSomBGM10x = "ES_Wind Drone Winter - SFX Producer - 1x.wav";
    public String ArquivoSomBGM15x = "ES_Wind Drone Winter - SFX Producer - 1.5x.wav";
    public String ArquivoSomBGM20x = "ES_Wind Drone Winter - SFX Producer - 2x.wav";

    // Variáveis de funcionamento interno. Evite alterar.

    public int CorrecaoX = 10;
    public int CorrecaoY = 0;
    public double AnguloVisao;
    public int Sair = 0;
    public long Pontuacao = 0;
    public long TempoR = System.currentTimeMillis();
    public long TempoR2 = System.currentTimeMillis();
    public String Espaco = "";
    public int FlagPausa = 1;
    public int FlagFlashCatch = 0;
    public int FlagCatchSound = 0;
    public int FlagBGM = 0;
    public Clip Catch = null;
    public Clip BGM = null;


    public double x = 0;
    public double y = 0;
    public double z = 0;
    public double xalvo;
    public double yalvo;
    public double zalvo;
    public double Teta = 0;
    public double Phi = 0;

    public int FlagArquivo;

    private static class LineType extends Object
        {
        final int x1; 
        final int y1;
        final int x2;
        final int y2;
        final Color color;

        public LineType(int x1, int y1, int x2, int y2, Color color)
            {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            }               
        }

    private final LinkedList<LineType> Linhas = new LinkedList<LineType>();
    private final LinkedList<LineType> LinhasGuia = new LinkedList<LineType>();

    public void addLine(int x1, int x2, int x3, int x4, Color color)
        {
        Linhas.add(new LineType(x1, x2, x3, x4, color));
        repaint();
        }

    public void addLineG(int x1, int x2, int x3, int x4, Color color)
        {
        LinhasGuia.add(new LineType(x1, x2, x3, x4, color));
        repaint();
        }

    public void clearLines()
        {
        Linhas.clear();
        repaint();
        }

    public void clearLinesG()
        {
        LinhasGuia.clear();
        repaint();
        }

    protected void paintComponent(Graphics g)
        {
        for (int i = 0; i < Linhas.size(); i++)
            {
            g.setColor(Linhas.get(i).color);
            g.drawLine(Linhas.get(i).x1, Linhas.get(i).y1, Linhas.get(i).x2, Linhas.get(i).y2);
            }

        for (int i = 0; i < LinhasGuia.size(); i++)
            {
            g.setColor(LinhasGuia.get(i).color);
            g.drawLine(LinhasGuia.get(i).x1, LinhasGuia.get(i).y1, LinhasGuia.get(i).x2, LinhasGuia.get(i).y2);
            }
        }

    public static void main (String[] args) {AV3DSpaceCatch mainc = new AV3DSpaceCatch(); if (args.length == 0) mainc.mainrun(""); else mainc.mainrun(args[0]);}

    public void mainrun (String ArquivoEstatisticas)
        {
        String Versao = "Versão desconhecida.";

        File fileVersao = new File(ArquivoAV3DSpaceCatchVersao);

        if (! ArquivoEstatisticas.equals(""))
            {Pontuacao = LerEstatisticas (ArquivoEstatisticas); FlagArquivo = 0;}

        if (Pontuacao < 0) Pontuacao = 0;

        try
            {
            BufferedReader brVersao = new BufferedReader(new FileReader(fileVersao));
            Versao = brVersao.readLine();
            } catch (IOException e) {}

        JFrame FameAV3DSpaceCatch = new JFrame("AV3DSpaceCatch - " + Versao);
        FameAV3DSpaceCatch.setIconImage(new ImageIcon(getClass().getResource(AV3DSpaceCatchIconFilePath)).getImage());

        FameAV3DSpaceCatch.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        FameAV3DSpaceCatch.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY + TamanhoEspacoLabelStatus));
        AV3DSpaceCatch comp = new AV3DSpaceCatch();
        comp.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY));
        FameAV3DSpaceCatch.getContentPane().add(comp, BorderLayout.PAGE_START);

        JLabel LabelStatus = new JLabel("");

        if (FlagPausa == 0)
            LabelStatus.setText("<html>Pontuação = " + String.valueOf(Pontuacao) + ".<br><br>Velocidade = " + String.valueOf(Velocidade) + "<br><br>Setas para direcionar.<br><br>\"A\" para aumentar velocidade. \"Z\" para reduzir.<br><br>\"P\" para pausar.<br><br>Barra de espaço para resetar as variáveis de localização.<br><br>ESC para sair.</html>");
        else
            LabelStatus.setText("<html>Pontuação: " + String.valueOf(Pontuacao) + ".<br><br>Jogo pausado.<br><br>Aperte \"P\" para continuar.</html>");

        LabelStatus.setFont(new Font("DialogInput", Font.BOLD | Font.ITALIC, TamanhoFonteLabelStatus));
        LabelStatus.setOpaque(true);
        LabelStatus.setLocation(5, TamanhoPlanoY + 5);
        FameAV3DSpaceCatch.add(LabelStatus);

        FameAV3DSpaceCatch.getContentPane().setBackground(Color.BLACK);

        FameAV3DSpaceCatch.addKeyListener(new KeyListener()
            {
            public void keyPressed(KeyEvent ke)
                {
                int keyCode = ke.getKeyCode();

                if (keyCode == KeyEvent.VK_ESCAPE)
                    Sair = 1;

                if (keyCode == KeyEvent.VK_SPACE)
                    {
                    x = 0;
                    y = 0;
                    z = 0;
                    Teta = 0;
                    Phi = 0;

                    FlagPausa = 1;
                    }

                if (keyCode == KeyEvent.VK_P)
                    if (FlagPausa == 1) FlagPausa = 0; else FlagPausa = 1;

                if (keyCode == KeyEvent.VK_A) if (FlagPausa == 0) 
                    if (Velocidade < LimiteSuperiorVelocidade)
                        {
                        Velocidade += 10;

                        while (! BGM.isRunning()) try{Thread.sleep(10);} catch (InterruptedException e) {}
                        BGM.stop();
                        BGM.close();
                        FlagBGM = 0;
                        }

                if (keyCode == KeyEvent.VK_Z) if (FlagPausa == 0) 
                    if (Velocidade > LimiteInferiorVelocidade)
                        {
                        Velocidade -= 10;

                        while (! BGM.isRunning()) try{Thread.sleep(10);} catch (InterruptedException e) {}
                        BGM.stop();
                        BGM.close();
                        FlagBGM = 0;
                        }

                if (keyCode == KeyEvent.VK_UP) if (FlagPausa == 0) 
                    {if (Math.abs(Phi) - DeslocamentoAngular <= Double.MAX_VALUE - DeslocamentoAngular) Phi += DeslocamentoAngular; else VariavelLimiteAtingido();}

                if (keyCode == KeyEvent.VK_DOWN) if (FlagPausa == 0) 
                    {if (Math.abs(Phi) - DeslocamentoAngular <= Double.MAX_VALUE - DeslocamentoAngular) Phi -= DeslocamentoAngular; else VariavelLimiteAtingido();}

                if (keyCode == KeyEvent.VK_LEFT) if (FlagPausa == 0) 
                    {if (Math.abs(Teta) - DeslocamentoAngular <= Double.MAX_VALUE - DeslocamentoAngular) Teta += DeslocamentoAngular; else VariavelLimiteAtingido();}

                if (keyCode == KeyEvent.VK_RIGHT) if (FlagPausa == 0) 
                    {if (Math.abs(Teta) - DeslocamentoAngular <= Double.MAX_VALUE - DeslocamentoAngular) Teta -= DeslocamentoAngular; else VariavelLimiteAtingido();}
                }

            public void keyReleased(KeyEvent ke){}
            public void keyTyped(KeyEvent ke){}
            });

        FameAV3DSpaceCatch.pack();
        FameAV3DSpaceCatch.setVisible(true);

        while (Sair == 0)
            {
            int FlagRedimensionarOver = 0;

            int width = FameAV3DSpaceCatch.getWidth();
            int height = FameAV3DSpaceCatch.getHeight();

            if (width < MinTamanhoPlanoX)
                {
                width = MinTamanhoPlanoX;
                FameAV3DSpaceCatch.setPreferredSize(new Dimension(width, height));
                FameAV3DSpaceCatch.setSize(width, height);
                FlagRedimensionarOver = 1;
                }

            if (height < MinTamanhoPlanoY)
                {
                height = MinTamanhoPlanoY;
                FameAV3DSpaceCatch.setPreferredSize(new Dimension(width, height));
                FameAV3DSpaceCatch.setSize(width, height);
                FlagRedimensionarOver = 1;
                }

            if (FlagRedimensionarOver == 0)
                if ((width != TamanhoPlanoX) || (height != TamanhoPlanoY + TamanhoEspacoLabelStatus))
                    {
                    TamanhoPlanoX = width;
                    TamanhoPlanoY = height - TamanhoEspacoLabelStatus;

                    FameAV3DSpaceCatch.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY + TamanhoEspacoLabelStatus));
                    comp.setPreferredSize(new Dimension(TamanhoPlanoX, TamanhoPlanoY));
                    FameAV3DSpaceCatch.pack();

                    x = 0;
                    y = 0;
                    z = 0;
                    Teta = 0;
                    Phi = 0;

                    FlagPausa = 1;
                    }

            try {
                if (FlagPausa == 0)
                    {
                    if (FlagBGM == 0)
                        {
                        InputStream BGMIS = null;

                        if ((Velocidade - LimiteInferiorVelocidade) / (LimiteSuperiorVelocidade - LimiteInferiorVelocidade) < 0.3)
                            BGMIS = getClass().getResourceAsStream(ArquivoSomBGM10x);
                        else if ((Velocidade - LimiteInferiorVelocidade) / (LimiteSuperiorVelocidade - LimiteInferiorVelocidade) < 0.6)
                            BGMIS = getClass().getResourceAsStream(ArquivoSomBGM15x);
                        else
                            BGMIS = getClass().getResourceAsStream(ArquivoSomBGM20x);

                        InputStream BGMBIS = new BufferedInputStream(BGMIS);
                        AudioInputStream BGMAIS = AudioSystem.getAudioInputStream(BGMBIS);
                        BGM = AudioSystem.getClip();  
                        BGM.open(BGMAIS);
                        BGM.loop(Clip.LOOP_CONTINUOUSLY);
                        BGM.start();
                        FlagBGM = 1;
                        }
                    }
                else if (FlagBGM == 1)
                    {
                    BGM.stop();
                    BGM.close();
                    FlagBGM = 0;
                    }
            } catch(Exception ex) {}

            long Tempo = System.currentTimeMillis();

            if (FlagPausa == 0) if (Tempo - TempoR > 1000 / Velocidade)
                {
                if ((Math.abs(x) - DeslocamentoLinear > Double.MAX_VALUE - DeslocamentoLinear) || (Math.abs(y) - DeslocamentoLinear > Double.MAX_VALUE - DeslocamentoLinear) || (Math.abs(z) - DeslocamentoLinear > Double.MAX_VALUE - DeslocamentoLinear))
                    VariavelLimiteAtingido();
                else
                    {
                    x += Math.cos(-Phi) * Math.cos(-Teta);
                    y += Math.cos(-Phi) * Math.sin(-Teta);
                    z += Math.sin(-Phi);
                    }

                TempoR = Tempo;
                }

            long Tempo2 = System.currentTimeMillis();

            if (FlagFlashCatch == 1) if ((Tempo2 - TempoR2) > 100) FlagFlashCatch = 0;

            if (FlagPausa == 0) if (Espaco.equals(""))
                {
                do
                    {
                    xalvo = (int) (Math.random() * LimiteXAlvo * Math.signum(Math.random() - 0.5));

                    yalvo = (int) (Math.random() * LimiteYAlvo * Math.signum(Math.random() - 0.5));

                    zalvo = (int) (Math.random() * LimiteZAlvo * Math.signum(Math.random() - 0.5));

                    } while (Math.sqrt(((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) * ((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) + ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) * ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) + ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z) * ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z)) <= DistanciaCapturaAlvo);


                Espaco = String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + "|" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + "|" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo + TamanhoAlvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + "|" + String.valueOf(xalvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo) + ";" + String.valueOf(xalvo + TamanhoAlvo) + "," + String.valueOf(yalvo) + "," + String.valueOf(zalvo + TamanhoAlvo);
                }

            if (FlagPausa == 0)
                {
                double FatorCor = Math.pow(Math.min(Math.sqrt(((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) * ((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) + ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) * ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) + ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z) * ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z)), (2 * Math.sqrt(3) * Math.max(LimiteXAlvo, Math.max(LimiteYAlvo, LimiteZAlvo)))) / (2 * Math.sqrt(3) * Math.max(LimiteXAlvo, Math.max(LimiteYAlvo, LimiteZAlvo))), 1 / FatorTonalidadeAproximacao);

                if (FlagPausa == 0) if (FlagFlashCatch == 0) FameAV3DSpaceCatch.getContentPane().setBackground(new Color((int) (64 - 64 * FatorCor), (int) (255 - 255 * FatorCor), (int) (64 - 64 * FatorCor)));
                }

            if (FlagPausa == 0)
                AnguloVisao = Math.atan(Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 / DistanciaTela);

            if (FlagPausa == 0) DesenharEspaco(comp);

            if (FlagPausa == 0) if (Math.sqrt(((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) * ((1 + Math.cos(-Phi) * Math.cos(-Teta)) * (2 * xalvo + TamanhoAlvo) / 2 - x) + ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) * ((1 + Math.cos(-Phi) * Math.sin(-Teta)) * (2 * yalvo + TamanhoAlvo) / 2 - y) + ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z) * ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (2 * zalvo + TamanhoAlvo) / 2 + z)) <= DistanciaCapturaAlvo)
                {
                try {
                    if (FlagCatchSound == 1) Catch.close();

                    InputStream CatchIS = getClass().getResourceAsStream(ArquivoSomCatch);
                    InputStream CatchBIS = new BufferedInputStream(CatchIS);
                    AudioInputStream CatchAIS = AudioSystem.getAudioInputStream(CatchBIS);
                    Catch = AudioSystem.getClip();  
                    Catch.open(CatchAIS);
                    Catch.start();

                    FlagCatchSound = 1;
                } catch(Exception ex) {}

                Pontuacao++;
                Espaco = "";
                FlagFlashCatch = 1;
                FameAV3DSpaceCatch.getContentPane().setBackground(new Color(0, 255, 0));
                TempoR2 = System.currentTimeMillis();

                if (FlagArquivo == 0)
                    EscreverEstatisticas(ArquivoEstatisticas, Pontuacao);

                System.out.println("Pontuação: " + String.valueOf(Pontuacao) + ".\n");
                }

            try {Thread.sleep(10);} catch(InterruptedException e) {}

            if (FlagPausa == 0)
                LabelStatus.setText("<html>Pontuação = " + String.valueOf(Pontuacao) + ".<br><br>Velocidade = " + String.valueOf(Velocidade) + "<br><br>Setas para direcionar.<br><br>\"A\" para aumentar velocidade. \"Z\" para reduzir.<br><br>\"P\" para pausar.<br><br>Barra de espaço para resetar as variáveis de localização.<br><br>ESC para sair.</html>");
            else
                LabelStatus.setText("<html>Pontuação: " + String.valueOf(Pontuacao) + ".<br><br>Jogo pausado.<br><br>Aperte \"P\" para continuar.</html>");
            }

        System.exit(0);
        }

    public void DesenharEspaco(AV3DSpaceCatch comp)
        {
        String [] EspacoStr2 = Espaco.split("@");

        String [] EspacoLinhas = EspacoStr2[0].split("\\|");

        comp.clearLines();
        comp.clearLinesG();

        for (int i = 0; i < EspacoLinhas.length; i++)
            {
            String [] Pontos = EspacoLinhas[i].split(";");

            String [] CoordenadasOrig = Pontos[0].split(",");
            String [] CoordenadasDest = Pontos[1].split(",");

            double xo = (1 + Math.cos(-Phi) * Math.cos(-Teta)) * Double.parseDouble(CoordenadasOrig[0]) - x;

            double xd = (1 + Math.cos(-Phi) * Math.cos(-Teta)) * Double.parseDouble(CoordenadasDest[0]) - x;

            double yo = (1 + Math.cos(-Phi) * Math.sin(-Teta)) * Double.parseDouble(CoordenadasOrig[1]) - y;

            double yd = (1 + Math.cos(-Phi) * Math.sin(-Teta)) * Double.parseDouble(CoordenadasDest[1]) - y;

            double zo = Math.cos(-Teta) * ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (-Double.parseDouble(CoordenadasOrig[2])) - z);

            double zd = Math.cos(-Teta) * ((1 + Math.sin(-Phi) * Math.cos(-Teta)) * (-Double.parseDouble(CoordenadasDest[2])) - z);

            int xi;
            int yi;
            int xf;
            int yf;

            if ((xo != 0) && (xd != 0))
                {
                xi = (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 + Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 * DistanciaTela * Math.tan(Math.atan(yo / xo) + Teta)) - CorrecaoX;

                xf = (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 + Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 * DistanciaTela * Math.tan(Math.atan(yd / xd) + Teta)) - CorrecaoX;

                yi = (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 + Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 * DistanciaTela * Math.tan(Math.atan(zo / xo) + Phi)) - CorrecaoY;

                yf = (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 + Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2 * DistanciaTela * Math.tan(Math.atan(zd / xd) + Phi)) - CorrecaoY;

                double ProdutoEscalaro = xo * Math.cos(-Teta) * Math.cos(-Phi) + yo * Math.sin(-Teta) * Math.cos(-Phi) + zo * Math.sin(-Phi) * Math.cos(-Teta);

                double ProdutoEscalard = xd * Math.cos(-Teta) * Math.cos(-Phi) + yd * Math.sin(-Teta) * Math.cos(-Phi) + zd * Math.sin(-Phi) * Math.cos(-Teta);

                double ProdutoEscalarXo = xo * Math.cos(-Teta) * Math.cos(-Phi) + yo * Math.sin(-Teta) * Math.cos(-Phi);

                double ProdutoEscalarXd = xd * Math.cos(-Teta) * Math.cos(-Phi) + yd * Math.sin(-Teta) * Math.cos(-Phi);

                double ProdutoEscalarZo = xo * Math.cos(-Teta) * Math.cos(-Phi) + zo * Math.sin(-Phi) * Math.cos(-Teta);

                double ProdutoEscalarZd = xd * Math.cos(-Teta) * Math.cos(-Phi) + zd * Math.sin(-Phi) * Math.cos(-Teta);

                if ((Math.acos(ProdutoEscalarXo / Math.sqrt(xo * xo + yo * yo)) <= 7 * Math.PI / 8) && (Math.acos(ProdutoEscalarXd / Math.sqrt(xd * xd + yd * yd)) <= 7 * Math.PI / 8))
                    {
                    if (Math.min(xi - CorrecaoX, xf - CorrecaoX) < 0)
                        {
                        comp.addLineG(50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoY, 40 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);

                        comp.addLineG(50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoY, 40 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);
                        }
        
                    if (Math.max(xi - CorrecaoX, xf - CorrecaoX) > Math.min(TamanhoPlanoX, TamanhoPlanoY))
                        {
                        comp.addLineG(Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoY, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40  - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);

                        comp.addLineG(Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoY, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40  - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);
                        }
                    }
                else if ((Math.acos(ProdutoEscalarXo / Math.sqrt(xo * xo + yo * yo)) > 7 * Math.PI / 8) || (Math.acos(ProdutoEscalarXd / Math.sqrt(xd * xd + yd * yd)) > 7 * Math.PI / 8))
                    {
                    comp.addLineG(50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoY, 40 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);

                    comp.addLineG(50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoY, 40 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);

                    comp.addLineG(Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoY, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40  - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);

                    comp.addLineG(Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoY, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40  - CorrecaoX, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoY, CorGuias);
                    }

                if ((Math.acos(ProdutoEscalarZo / Math.sqrt(xo * xo + zo * zo)) <= 7 * Math.PI / 8) && (Math.acos(ProdutoEscalarZd / Math.sqrt(xd * xd + zd * zd)) <= 7 * Math.PI / 8))
                    {
                    if (Math.max(yi - CorrecaoY, yf - CorrecaoY) > Math.min(TamanhoPlanoX, TamanhoPlanoY))
                        {
                        comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40 - CorrecaoY, CorGuias);

                        comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoY, CorGuias);
                        }
        
                    if (Math.min(yi - CorrecaoY, yf - CorrecaoY) < 0)
                        {
                        comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoX, 50 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, 40 - CorrecaoY, CorGuias);

                        comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, 40 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoX, 50 - CorrecaoY, CorGuias);
                        }
                    }
                else if ((Math.acos(ProdutoEscalarZo / Math.sqrt(xo * xo + zo * zo)) > 7 * Math.PI / 8) || (Math.acos(ProdutoEscalarZd / Math.sqrt(xd * xd + zd * zd)) > 7 * Math.PI / 8))
                    {
                    comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40 - CorrecaoY, CorGuias);

                    comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 40 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoX, Math.min(TamanhoPlanoX, TamanhoPlanoY) - 50 - CorrecaoY, CorGuias);

                    comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - 20 - CorrecaoX, 50 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, 40 - CorrecaoY, CorGuias);

                    comp.addLineG((int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) - CorrecaoX, 40 - CorrecaoY, (int) (Math.min(TamanhoPlanoX, TamanhoPlanoY) / 2) + 20 - CorrecaoX, 50 - CorrecaoY, CorGuias);
                    }

                if ((ProdutoEscalaro > 0) && (ProdutoEscalard > 0) && (Math.acos(ProdutoEscalaro / Math.sqrt(xo * xo + yo * yo + zo * zo)) < AnguloVisao) && (Math.acos(ProdutoEscalard / Math.sqrt(xd * xd + yd * yd + zd * zd)) < AnguloVisao) && (Math.min(xi, Math.min(yi, Math.min(xf, yf))) > 0) && (Math.max(xi, Math.max(yi, Math.max(xf, yf))) < Math.min(TamanhoPlanoX, TamanhoPlanoY)))
                    comp.addLine(xi, yi, xf, yf, CorAlvo);
                }
            }
        }

    public long LerEstatisticas(String ArquivoEstatisticasArg)
        {
        File file = new File(ArquivoEstatisticasArg);

        try
            {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String PontuacaoStr = "";
            PontuacaoStr = br.readLine();
            if (AntonioVandre.NumeroInteiroLong(PontuacaoStr))
                return Long.valueOf(PontuacaoStr);
            else
                return -1;
            } catch (IOException e) {return -1;}
        }

    public void EscreverEstatisticas (String ArquivoEstatisticasArg, long PontuacaoArg)
        {
        try
            {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ArquivoEstatisticasArg));
            writer.write(String.valueOf(PontuacaoArg));
            writer.close();
            }
        catch (IOException e) {}
        }

    public void VariavelLimiteAtingido()
        {
        x = 0;
        y = 0;
        z = 0;
        Teta = 0;
        Phi = 0;
        }
    }
