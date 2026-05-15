package com.deploytest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet principal para validar el pipeline CI/CD.
 * Responde en la ruta raíz "/" con una página HTML
 * que confirma el despliegue correcto y el estado de la base de datos.
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

        // --- Verificar conexión a MySQL ---
        String dbUrl = System.getenv("DB_URL") != null
                ? System.getenv("DB_URL")
                : "jdbc:mysql://mysql_global:3306/deploy_test";
        String dbUser = System.getenv("DB_USER") != null
                ? System.getenv("DB_USER")
                : "root";
        String dbPassword = System.getenv("DB_PASSWORD") != null
                ? System.getenv("DB_PASSWORD")
                : "root2026.";

        boolean dbConnected = false;
        String dbVersion = "N/A";
        String dbHost = "N/A";
        String dbError = "";
        long dbResponseTime = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            long start = System.currentTimeMillis();
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                dbResponseTime = System.currentTimeMillis() - start;
                dbConnected = true;
                DatabaseMetaData meta = conn.getMetaData();
                dbVersion = meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion();
                dbHost = meta.getURL();
            }
        } catch (Exception e) {
            dbError = e.getMessage();
        }

        String dbStatusIcon = dbConnected ? "✅" : "❌";
        String dbStatusText = dbConnected ? "Conectado" : "Sin conexión";
        String dbStatusColor = dbConnected
                ? "linear-gradient(90deg, #00f260, #0575e6)"
                : "linear-gradient(90deg, #ff416c, #ff4b2b)";
        String dbBorderColor = dbConnected
                ? "rgba(0, 242, 96, 0.3)"
                : "rgba(255, 65, 108, 0.3)";

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>Deploy Test - CI/CD Pipeline</title>");
            out.println("    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">");
            out.println("    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap\" rel=\"stylesheet\">");
            out.println("    <style>");
            out.println("        * { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("        body {");
            out.println("            font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            out.println("            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);");
            out.println("            min-height: 100vh;");
            out.println("            display: flex;");
            out.println("            align-items: center;");
            out.println("            justify-content: center;");
            out.println("            color: #ffffff;");
            out.println("            padding: 20px;");
            out.println("        }");
            out.println("        .wrapper {");
            out.println("            display: flex;");
            out.println("            flex-direction: column;");
            out.println("            gap: 24px;");
            out.println("            max-width: 560px;");
            out.println("            width: 100%;");
            out.println("        }");
            out.println("        .card {");
            out.println("            text-align: center;");
            out.println("            background: rgba(255,255,255,0.05);");
            out.println("            backdrop-filter: blur(10px);");
            out.println("            border: 1px solid rgba(255,255,255,0.1);");
            out.println("            border-radius: 20px;");
            out.println("            padding: 48px 48px;");
            out.println("            box-shadow: 0 8px 32px rgba(0,0,0,0.3);");
            out.println("            transition: transform 0.3s ease, box-shadow 0.3s ease;");
            out.println("        }");
            out.println("        .card:hover {");
            out.println("            transform: translateY(-4px);");
            out.println("            box-shadow: 0 16px 48px rgba(0,0,0,0.4);");
            out.println("        }");
            out.println("        .status-icon {");
            out.println("            font-size: 56px;");
            out.println("            margin-bottom: 16px;");
            out.println("            animation: pulse 2s infinite;");
            out.println("        }");
            out.println("        @keyframes pulse {");
            out.println("            0%, 100% { transform: scale(1); }");
            out.println("            50% { transform: scale(1.1); }");
            out.println("        }");
            out.println("        h1 {");
            out.println("            font-size: 40px;");
            out.println("            font-weight: 700;");
            out.println("            background: linear-gradient(90deg, #00f260, #0575e6);");
            out.println("            -webkit-background-clip: text;");
            out.println("            -webkit-text-fill-color: transparent;");
            out.println("            background-clip: text;");
            out.println("            margin-bottom: 24px;");
            out.println("        }");
            out.println("        h2 {");
            out.println("            font-size: 20px;");
            out.println("            font-weight: 600;");
            out.println("            margin-bottom: 20px;");
            out.println("            display: flex;");
            out.println("            align-items: center;");
            out.println("            justify-content: center;");
            out.println("            gap: 10px;");
            out.println("        }");
            out.println("        .info {");
            out.println("            font-size: 14px;");
            out.println("            color: rgba(255,255,255,0.6);");
            out.println("            line-height: 2;");
            out.println("            text-align: left;");
            out.println("        }");
            out.println("        .info .row {");
            out.println("            display: flex;");
            out.println("            justify-content: space-between;");
            out.println("            align-items: center;");
            out.println("            padding: 6px 0;");
            out.println("            border-bottom: 1px solid rgba(255,255,255,0.05);");
            out.println("        }");
            out.println("        .info .row:last-child { border-bottom: none; }");
            out.println("        .info .label { color: rgba(255,255,255,0.5); font-weight: 300; }");
            out.println("        .info .value { color: rgba(255,255,255,0.95); font-weight: 600; text-align: right; max-width: 60%; word-break: break-all; }");
            out.println("        .badge {");
            out.println("            display: inline-block;");
            out.println("            margin-top: 24px;");
            out.println("            padding: 8px 24px;");
            out.println("            border-radius: 50px;");
            out.println("            font-size: 11px;");
            out.println("            font-weight: 700;");
            out.println("            text-transform: uppercase;");
            out.println("            letter-spacing: 2px;");
            out.println("        }");
            out.println("        .db-card { border-color: " + dbBorderColor + "; }");
            out.println("        .db-status-badge {");
            out.println("            background: " + dbStatusColor + ";");
            out.println("        }");
            out.println("        .pipeline-badge {");
            out.println("            background: linear-gradient(90deg, #00f260, #0575e6);");
            out.println("        }");
            out.println("        .error-msg {");
            out.println("            margin-top: 16px;");
            out.println("            padding: 12px 16px;");
            out.println("            background: rgba(255, 65, 108, 0.1);");
            out.println("            border: 1px solid rgba(255, 65, 108, 0.2);");
            out.println("            border-radius: 12px;");
            out.println("            font-size: 12px;");
            out.println("            color: #ff6b81;");
            out.println("            text-align: left;");
            out.println("            word-break: break-all;");
            out.println("            font-family: 'Courier New', monospace;");
            out.println("        }");
            out.println("    </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("    <div class=\"wrapper\">");

            // --- Card del servidor ---
            out.println("        <div class=\"card\">");
            out.println("            <div class=\"status-icon\">✅</div>");
            out.println("            <h1>Deploy Correcto</h1>");
            out.println("            <div class=\"info\">");
            out.println("                <div class=\"row\"><span class=\"label\">Servidor</span><span class=\"value\">" + serverInfo + "</span></div>");
            out.println("                <div class=\"row\"><span class=\"label\">Java</span><span class=\"value\">" + javaVersion + "</span></div>");
            out.println("                <div class=\"row\"><span class=\"label\">Timestamp</span><span class=\"value\">" + timestamp + "</span></div>");
            out.println("            </div>");
            out.println("            <div class=\"badge pipeline-badge\">Pipeline CI/CD Activo</div>");
            out.println("        </div>");

            // --- Card de la base de datos ---
            out.println("        <div class=\"card db-card\">");
            out.println("            <h2><span style=\"font-size:32px\">" + dbStatusIcon + "</span> Base de Datos</h2>");
            out.println("            <div class=\"info\">");
            out.println("                <div class=\"row\"><span class=\"label\">Estado</span><span class=\"value\">" + dbStatusText + "</span></div>");
            out.println("                <div class=\"row\"><span class=\"label\">URL</span><span class=\"value\">" + dbUrl + "</span></div>");
            if (dbConnected) {
                out.println("                <div class=\"row\"><span class=\"label\">Versión</span><span class=\"value\">" + dbVersion + "</span></div>");
                out.println("                <div class=\"row\"><span class=\"label\">Tiempo de respuesta</span><span class=\"value\">" + dbResponseTime + " ms</span></div>");
            }
            out.println("            </div>");
            if (!dbConnected && !dbError.isEmpty()) {
                out.println("            <div class=\"error-msg\">Error: " + dbError + "</div>");
            }
            out.println("            <div class=\"badge db-status-badge\">" + dbStatusText + "</div>");
            out.println("        </div>");

            out.println("    </div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
