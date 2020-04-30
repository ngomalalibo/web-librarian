package com.pc.weblibrarian.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThumbMaker {

	/*public static void main(String[] args) throws Exception {

		File file = new File("C:\\tmp\\cover.jpg");

		try (FileInputStream fis = new FileInputStream(file)) {
			File fileo = new File("C:\\tmp\\coverout.jpg");
			try (FileOutputStream fos = new FileOutputStream(fileo)) {
				byte[] bytez = getThumb(fis, 100);
				fos.write(bytez);
				fos.flush();
			}
		}
	}*/

    
    public static byte[] getThumb(byte[] imgstr, int px, boolean noalpha) {
        ByteArrayOutputStream baos = null;
        try
        {
            boolean box = true;
            ImageIO.setUseCache(false);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgstr));
            int w = image.getWidth();
            int h = image.getHeight();
        
            Image oimg;
            if (w > px || h > px) {
                if (w > h) {
                    oimg = image.getScaledInstance(px, -1, Image.SCALE_AREA_AVERAGING);
                } else {
                    oimg = image.getScaledInstance(-1, px, Image.SCALE_AREA_AVERAGING);
                }
            } else {
                oimg = image;
            }
        
            int ow = oimg.getWidth(null);
            int oh = oimg.getHeight(null);
        
            BufferedImage outimg;
        
            baos = new ByteArrayOutputStream(px * px * 4);
            if (box) {
                outimg = new BufferedImage(px, px, noalpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
                outimg.createGraphics().drawImage(oimg, px / 2 - ow / 2, px / 2 - oh / 2, null);
            } else {
                outimg = new BufferedImage(ow, oh, noalpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
                outimg.createGraphics().drawImage(oimg, 0, 0, null);
            }
            ImageIO.write(outimg, "png", baos);
        }
        catch (IOException e)
        {
            e.getMessage();
        }
        return baos.toByteArray();
    }
    
    public static byte[] getStringThumb(String text, int tpx) {
        boolean box = false;
        int px = 256;
        try {
            if (tpx > 256 || tpx < 0)
                tpx = 256;
            
            if (text.length() > 2)
                text = text.substring(0, 2);
            
            ImageIO.setUseCache(false);
            BufferedImage outimg = new BufferedImage(px, px, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gpx = (Graphics2D) outimg.getGraphics();
            gpx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gpx.setColor(new Color(34, 51, 72));
            
            if (box)
                gpx.fillRect(0, 0, px, px);
            else
                gpx.fillOval(0, 0, px, px);
            
            gpx.setColor(Color.WHITE);
            
            gpx.setFont(gpx.getFont().deriveFont(Font.PLAIN, px / 2));
            
            Rectangle2D rect = gpx.getFont().getStringBounds(text, gpx.getFontRenderContext());
            
            int tw = (int) rect.getWidth();
            int th = (int) rect.getHeight();
            gpx.drawString(text, px / 2 - tw / 2, px - th / 2);
            
            Image image = outimg.getScaledInstance(tpx, tpx, Image.SCALE_AREA_AVERAGING);
            
            BufferedImage u = new BufferedImage(tpx, tpx, BufferedImage.TYPE_INT_ARGB);
            u.createGraphics().drawImage(image, 0, 0, null);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream(tpx * tpx * 4);
            ImageIO.write(u, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
    
}
