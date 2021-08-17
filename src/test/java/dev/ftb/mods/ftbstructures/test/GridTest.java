package dev.ftb.mods.ftbstructures.test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GridTest {
	public static void main(String[] args) throws Exception {
		int s = 44;

		BufferedImage image = new BufferedImage(512 * s, 512 * s, BufferedImage.TYPE_INT_RGB);

		IslandGridBiomeTestSource source = new IslandGridBiomeTestSource(4, 0xA3BE8C,
				new BiomeTestEntry(0x5E81AC, 512 * 1.2D),
				new BiomeTestEntry(0x81A1C1, 512 * 0.7D),
				new BiomeTestEntry(0x88C0D0, 512 * 0.2D)
		);

		for (int y = 0; y < 512 * s; y++) {
			for (int x = 0; x < 512 * s; x++) {
				image.setRGB(x, y, source.getNoiseBiome((x - 256 * s) >> 2, (y - 256 * s) >> 2));
			}
		}

		Graphics2D g = image.createGraphics();
		g.setColor(new Color(0x2E3440));
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 100));

		for (int y = 0; y < s; y++) {
			for (int x = 0; x < s; x++) {
				g.drawString(((x - s / 2) * 512 + 256) + "," + ((y - s / 2) * 512 + 256), x * 512 + 10, y * 512 + 80);
			}

			g.drawLine(0, y * 512, s * 512, y * 512);
			g.drawLine(0, y * 512 + 511, s * 512, y * 512 + 511);
			g.drawLine(y * 512, 0, y * 512, s * 512);
			g.drawLine(y * 512 + 511, 0, y * 512 + 511, s * 512);
		}

		g.dispose();

		ImageIO.write(image, "PNG", Files.newOutputStream(Paths.get("grid_test.png")));
	}
}
