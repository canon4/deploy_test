package com.deploytest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet principal para validar el pipeline CI/CD.
 * Responde en la ruta raíz "/" con una página HTML
 * que confirma el despliegue correcto.
 */
@WebServlet("/")
public class DeployServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String serverInfo = getServletContext().getServerInfo();
        String javaVersion = System.getProperty("java.version");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>Deploy Test - CI/CD Pipeline</title>");
            out.println("    <style>");
            out.println("        * { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("        body {");
            out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            out.println("            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);");
            out.println("            min-height: 100vh;");
            out.println("            display: flex;");
            out.println("            align-items: center;");
            out.println("            justify-content: center;");
            out.println("            color: #ffffff;");
            out.println("        }");
            out.println("        .container {");
            out.println("            text-align: center;");
            out.println("            background: rgba(255,255,255,0.05);");
            out.println("            backdrop-filter: blur(10px);");
            out.println("            border: 1px solid rgba(255,255,255,0.1);");
            out.println("            border-radius: 20px;");
            out.println("            padding: 60px 80px;");
            out.println("            box-shadow: 0 8px 32px rgba(0,0,0,0.3);");
            out.println("        }");
            out.println("        .status-icon {");
            out.println("            font-size: 64px;");
            out.println("            margin-bottom: 20px;");
            out.println("            animation: pulse 2s infinite;");
            out.println("        }");
            out.println("        @keyframes pulse {");
            out.println("            0%, 100% { transform: scale(1); }");
            out.println("            50% { transform: scale(1.1); }");
            out.println("        }");
            out.println("        h1 {");
            out.println("            font-size: 48px;");
            out.println("            font-weight: 700;");
            out.println("            background: linear-gradient(90deg, #00f260, #0575e6);");
            out.println("            -webkit-background-clip: text;");
            out.println("            -webkit-text-fill-color: transparent;");
            out.println("            background-clip: text;");
            out.println("            margin-bottom: 30px;");
            out.println("        }");
            out.println("        .info {");
            out.println("            font-size: 14px;");
            out.println("            color: rgba(255,255,255,0.6);");
            out.println("            line-height: 1.8;");
            out.println("        }");
            out.println("        .info span {");
            out.println("            color: rgba(255,255,255,0.9);");
            out.println("            font-weight: 600;");
            out.println("        }");
            out.println("        .badge {");
            out.println("            display: inline-block;");
            out.println("            margin-top: 25px;");
            out.println("            padding: 8px 20px;");
            out.println("            background: linear-gradient(90deg, #00f260, #0575e6);");
            out.println("            border-radius: 50px;");
            out.println("            font-size: 12px;");
            out.println("            font-weight: 700;");
            out.println("            text-transform: uppercase;");
            out.println("            letter-spacing: 2px;");
            out.println("        }");
            out.println("    </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("    <div class=\"container\">");
            out.println("        <div class=\"status-icon\">✅</div>");
            out.println("        <h1>Deploy Correcto</h1>");
            out.println("        <div class=\"info\">");
            out.println("            <p>Servidor: <span>" + serverInfo + "</span></p>");
            out.println("            <p>Java: <span>" + javaVersion + "</span></p>");
            out.println("            <p>Timestamp: <span>" + timestamp + "</span></p>");
            out.println("        </div>");
            out.println("        <div class=\"badge\">Pipeline CI/CD Activo</div>");
            out.println("    </div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
