package com.amituofo.common.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.swing.Icon;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.parser.SVGLoader;

public final class FileExtSvgIconBuilder {
	private static final String SVG_NS = "http://www.w3.org/2000/svg";

	private FileExtSvgIconBuilder() {
	}

	public static Icon createFileExtIcon(String ext, int width, int height) {
		try {
			Document svgDom = createSvgDocument(ext, width, height);
			byte[] svgBytes = toBytes(svgDom);

			SVGLoader loader = new SVGLoader();
			SVGDocument svgDocument = loader.load(new ByteArrayInputStream(svgBytes));

			if (svgDocument == null) {
				throw new IllegalStateException("Failed to load generated SVG.");
			}

			return new JsvgIcon(svgDocument, width, height);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to create SVG icon.", e);
		}
	}

	private static Document createSvgDocument(String ext, int width, int height) throws Exception {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("width and height must be > 0");
		}

		String text = normalizeExt(ext);
		boolean isTiny = width <= 18 || height <= 18;
		String displayText = isTiny && !text.isEmpty() ? text.substring(0, 1) : text;

		Color color = computeColor(text);

		int barH = isTiny ? Math.max(4, height / 4) : Math.max(5, height / 10);
		int barPadding = isTiny ? 2 : Math.max(2, width / 8);
		double arc = Math.min(height * 0.25, 10.0);
		double fontSize = isTiny ? height * 0.60 : height * 0.22;
		double textY = isTiny ? (height - barH) * 0.52 : (height - barH - 10) * 0.54;

		String suffix = Integer.toHexString((text + width + "x" + height).hashCode());
		String glassGradientId = "glass-" + suffix;
		String barGradientId = "bar-" + suffix;
		String clipId = "clip-" + suffix;

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element svg = element(doc, "svg");
		svg.setAttribute("width", String.valueOf(width));
		svg.setAttribute("height", String.valueOf(height));
		svg.setAttribute("viewBox", "0 0 " + width + " " + height);
		doc.appendChild(svg);

		Element defs = element(doc, "defs");
		svg.appendChild(defs);

		Element glassGradient = element(doc, "linearGradient");
		glassGradient.setAttribute("id", glassGradientId);
		glassGradient.setAttribute("x1", "0");
		glassGradient.setAttribute("y1", "0");
		glassGradient.setAttribute("x2", "0");
		glassGradient.setAttribute("y2", "1");
		defs.appendChild(glassGradient);

		appendStop(doc, glassGradient, "0%", "#FFFFFF", "1");
		appendStop(doc, glassGradient, "40%", "#FCFDFF", "0.94");
		appendStop(doc, glassGradient, "100%", "#F5F7FA", "0.84");

		Element barGradient = element(doc, "linearGradient");
		barGradient.setAttribute("id", barGradientId);
		barGradient.setAttribute("x1", "0");
		barGradient.setAttribute("y1", "0");
		barGradient.setAttribute("x2", "1");
		barGradient.setAttribute("y2", "1");
		defs.appendChild(barGradient);

		appendStop(doc, barGradient, "0%", lighten(color, 0.18), "1");
		appendStop(doc, barGradient, "100%", darken(color, 0.08), "1");

		Element clipPath = element(doc, "clipPath");
		clipPath.setAttribute("id", clipId);
		defs.appendChild(clipPath);

		Element clipRect = element(doc, "rect");
		setRectAttrs(clipRect, 1, 1, width - 2, height - 2);
		clipRect.setAttribute("rx", num(arc));
		clipRect.setAttribute("ry", num(arc));
		clipPath.appendChild(clipRect);

		Element body = element(doc, "rect");
		setRectAttrs(body, 1, 1, width - 2, height - 2);
		body.setAttribute("rx", num(arc));
		body.setAttribute("ry", num(arc));
		body.setAttribute("fill", "url(#" + glassGradientId + ")");
		body.setAttribute("stroke", "#BEC8D2");
		body.setAttribute("stroke-opacity", "0.55");
		body.setAttribute("stroke-width", "0.7");
		svg.appendChild(body);

		Element barGroup = element(doc, "g");
		barGroup.setAttribute("clip-path", "url(#" + clipId + ")");
		svg.appendChild(barGroup);

		Element bar = element(doc, "rect");
		if (isTiny) {
			setRectAttrs(bar, 0, height - barH, width, barH);
		} else {
			int barWidth = width - barPadding * 2;
			setRectAttrs(bar, barPadding, height - barH - 8, barWidth, barH);

			double barRadius = Math.min(barH, barWidth) / 2.0;
			bar.setAttribute("rx", num(barRadius));
			bar.setAttribute("ry", num(barRadius));
		}
		bar.setAttribute("fill", "url(#" + barGradientId + ")");
		barGroup.appendChild(bar);

		Element textEl = element(doc, "text");
		textEl.setAttribute("x", "50%");
		textEl.setAttribute("y", num(textY));
		textEl.setAttribute("text-anchor", "middle");
		textEl.setAttribute("dominant-baseline", "middle");
		textEl.setAttribute("font-family", "Arial, sans-serif");
		textEl.setAttribute("font-size", num(fontSize));
		textEl.setAttribute("font-weight", "700");
		textEl.setAttribute("fill", "#333C44");
		textEl.setTextContent(displayText);
		svg.appendChild(textEl);

		return doc;
	}

	private static Element element(Document doc, String name) {
		return doc.createElementNS(SVG_NS, name);
	}

	private static void appendStop(Document doc, Element gradient, String offset, String color, String opacity) {
		Element stop = element(doc, "stop");
		stop.setAttribute("offset", offset);
		stop.setAttribute("stop-color", color);
		stop.setAttribute("stop-opacity", opacity);
		gradient.appendChild(stop);
	}

	private static void setRectAttrs(Element rect, int x, int y, int width, int height) {
		rect.setAttribute("x", String.valueOf(x));
		rect.setAttribute("y", String.valueOf(y));
		rect.setAttribute("width", String.valueOf(width));
		rect.setAttribute("height", String.valueOf(height));
	}

	private static byte[] toBytes(Document doc) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		TransformerFactory factory = TransformerFactory.newInstance();
		javax.xml.transform.Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

		transformer.transform(new DOMSource(doc), new StreamResult(out));
		return out.toByteArray();
	}

	private static String normalizeExt(String ext) {
		if (ext == null) {
			return "";
		}

		ext = ext.trim();
		if (ext.startsWith(".")) {
			ext = ext.substring(1);
		}
		if (ext.length() > 4) {
			ext = ext.substring(0, 4);
		}

		return ext.toUpperCase(Locale.ROOT);
	}

	private static Color computeColor(String text) {
		int hash = text == null ? 0 : text.hashCode();
		float hue = Math.abs(hash % 360) / 360f;
		return Color.getHSBColor(hue, 0.6f, 0.95f);
	}

	private static String lighten(Color color, double amount) {
		return mix(color, Color.WHITE, amount);
	}

	private static String darken(Color color, double amount) {
		return mix(color, Color.BLACK, amount);
	}

	private static String mix(Color source, Color target, double amount) {
		amount = Math.max(0, Math.min(1, amount));

		int r = (int) Math.round(source.getRed() + (target.getRed() - source.getRed()) * amount);
		int g = (int) Math.round(source.getGreen() + (target.getGreen() - source.getGreen()) * amount);
		int b = (int) Math.round(source.getBlue() + (target.getBlue() - source.getBlue()) * amount);

		return String.format("#%02X%02X%02X", r, g, b);
	}

	private static String num(double value) {
		return String.format(Locale.ROOT, "%.2f", value);
	}

	private static final class JsvgIcon implements Icon {
		private final SVGDocument document;
		private final int width;
		private final int height;

		private JsvgIcon(SVGDocument document, int width, int height) {
			this.document = document;
			this.width = width;
			this.height = height;
		}

		@Override
		public int getIconWidth() {
			return width;
		}

		@Override
		public int getIconHeight() {
			return height;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g.create();
			try {
				g2.translate(x, y);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

				document.render(c, g2, new ViewBox(0, 0, width, height));
			} finally {
				g2.dispose();
			}
		}
	}
}
