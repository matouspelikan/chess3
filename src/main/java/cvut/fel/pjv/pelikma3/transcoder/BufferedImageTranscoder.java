//package cvut.fel.pjv.pelikma3.transcoder;
//
//import org.apache.batik.transcoder.TranscoderException;
//import org.apache.batik.transcoder.TranscoderOutput;
//import org.apache.batik.transcoder.image.ImageTranscoder;
//
//import java.awt.image.BufferedImage;
//
//public class BufferedImageTranscoder extends ImageTranscoder {
//
//    private BufferedImage bufferedImage;
//    public BufferedImage getBufferedImage(){
//        return this.bufferedImage;
//    }
//
//    @Override
//    public BufferedImage createImage(int width, int height) {
//        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//    }
//
//    @Override
//    public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) throws TranscoderException {
//        this.bufferedImage = bufferedImage;
//    }
//}
