/**
* 2017-11-29
* RandomCodeController.java
* author:ys
*/ 
package com.thinkgem.jeesite.modules.inxedu.web;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping("${frontPath}")
public class RandomCodeController extends BaseController{
	@RequestMapping("/ran/random")
    public void genericRandomCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setHeader("Cache-Control", "private,no-cache,no-store");
    response.setContentType("image/png");
    HttpSession session = request.getSession();
    int width = 85;
    int height = 28;
    BufferedImage image = new BufferedImage(width, height, 2);
    Graphics2D g = image.createGraphics();
    g.setComposite(AlphaComposite.getInstance(3, 1.0f));
    Random random = new Random();
    g.setColor(new Color(231, 231, 231));
    g.fillRect(0, 0, width, height);
    g.setFont(new Font("Microsoft YaHei", 2, 24));
    String sRand = "";
    for (int responseOutputStream = 0; responseOutputStream < 4; ++responseOutputStream) {
        String rand = String.valueOf(random.nextInt(10));
        sRand = sRand + rand;
        g.setColor(new Color(121, 143, 96));
        g.drawString(rand, 13 * responseOutputStream + 16, 23);
    }
    session.setAttribute("COMMON_RAND_CODE", (Object)sRand);
    g.dispose();
    ServletOutputStream var12 = response.getOutputStream();
    ImageIO.write((RenderedImage)image, "png", (OutputStream)var12);
    var12.close();
    }
}
