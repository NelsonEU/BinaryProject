package ihm;

import biz.dto.ITournamentDto;
import biz.dto.IUserDto;
import biz.ucc.ITournamentUcc;
import biz.ucc.IUserUcc;
import biz.ucc.TournamentUcc;
import biz.ucc.UserUcc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.JWTSigner;
import com.owlike.genson.Genson;
import exceptions.BizException;
import exceptions.FatalException;


public class Dispatcher {

    private static final String ENCODED_JWT = "encodedJWT";
    private IUserUcc userUcc;
    private IUserDto user;
    private Genson genson;
    private String jwtSecret;
    private ITournamentUcc tournamentUcc;

    public Dispatcher() {
        this.userUcc = new UserUcc();
        this.tournamentUcc = new TournamentUcc();
//        this.genson = new GensonBuilder().useConstructorWithArguments(true).withBundle(new JavaDateTimeBundle()).create();
        this.genson = new Genson();
        this.jwtSecret = "fsdfgjkladhsf";
    }

    public void operer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
//        String userJson = req.getParameter("email");
        HttpSession session = null;
        String signature = null;
        try {
            if (action == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action invalide");
                return;
            }
            switch (action) {
                case "isConnected":
                    if(userConnected(req, resp)){
                        sendResponse(resp, "Connected", 200);
                    }else{
                        sendResponse(resp, "Not connected", 402);
                    }
                    break;
                case "login":
                    login(req, resp, session);
                    break;
                case "register":
                    register(req, resp, session);
                    break;
                case "logout":
                    logout(req, resp, session);
                    break;
                case "getTournaments":
                    getTournaments(req, resp, session);
                    break;
                default:
                    if(userConnected(req,resp)){
                        switch(action){
                            case "registerTournament":
                                registerTournament(req, resp);
                                break;
                        }
                    }else {
                        sendResponse(resp, "Unauthorized", 401);
                    }
                    break;
            }
        } catch (IOException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: operer : "
//                    + exception.getMessage() + "-----");
            sendResponse(resp, exception.getMessage(), 002);
        } catch (IllegalStateException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: operer : "
//                    + exception.getMessage() + "-----");
            sendResponse(resp, exception.getMessage(), 500);
        } catch (Exception exception) {
            exception.printStackTrace();
//            logger.info("Exception lancée lors de l'appel de la methode: operer : "
//                    + exception.getMessage() + "-----");
            sendResponse(resp, exception.getMessage(), 500);

        }
    }

    private void registerTournament(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = this.user.getUserId();
        int tournamentId = Integer.parseInt(req.getParameter("tournamentId"));
        try {
            this.tournamentUcc.register(userId, tournamentId);
            resp.setStatus(HttpServletResponse.SC_OK);
        }catch(FatalException e){
            resp.sendError(500, "Something went wrong");
        }

    }

    private boolean userConnected(HttpServletRequest req, HttpServletResponse resp) {
        return this.user != null;
    }

    private void getTournaments(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws InterruptedException, IOException {
        List<ITournamentDto> list = new ArrayList<>();
        if(this.user != null) {
            list = this.tournamentUcc.get24hTournaments(this.user.getUserId());
        }else{
            list = this.tournamentUcc.get24hTournaments(-1);
        }
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().print(this.genson.serialize(list));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {

        session = req.getSession(false);

        if (session != null) {
            // Supression du JSESSIONID (automatique si le client quitte le
            // navigateur).
            session.invalidate();
            Cookie[] cookies = req.getCookies();
            for (Cookie c : cookies) {
                // Suppression de tous les cookies.
                this.setCookie(c.getName(), null, resp, 0);
            }
            this.user = null;
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Tranquille");
//            logger.info("logout : utilisateur déconnecté.");
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        try {


            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String username = req.getParameter("username");
            this.user = this.userUcc.register(email, password, username);
            if (this.user != null) {
                this.encodeJwt(req, resp, session);
                resp.flushBuffer();
            } else {
                System.out.println("ON EST USER NULL");
                sendResponse(resp,
                        "BizException : Probleme", 400);
            }
        } catch (FatalException e) {
            sendResponse(resp, "FatalException : erreur lors de l'inscription d'un utilisateur "
                    + e.getMessage(), 500);
        } catch (BizException exception) {
            resp.setStatus(422);
            resp.getOutputStream().print(exception.getMessage());
            resp.flushBuffer();
        }
    }


    private void login(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            this.user = this.userUcc.login(email, password);
            if (this.user != null) {
                System.out.println(this.user);
                System.out.println(this.genson.serialize(this.user));
                System.out.println("ON EST USER NOT NULL");
                this.encodeJwt(req, resp, session);
//                resp.getOutputStream().print(this.genson.serialize(this.user));
                resp.flushBuffer();
            } else {
                System.out.println("ON EST USER NULL");
                sendResponse(resp,
                        "BizException : Probleme", 400);
            }
        } catch (FatalException e) {
            sendResponse(resp, "Erreur fatale: " + e.getMessage(), 500);
        } catch (BizException exception) {
            resp.setStatus(422);
            resp.getOutputStream().print(exception.getMessage());
            resp.flushBuffer();
        }
    }

    private String encodeJwt(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        // Jwt payload.
        Map<String, Object> payload = new HashMap<String, Object>();

        payload.put("userId", this.user.getUserId());
        // IP de l'utilisateur
        payload.put("ip", req.getRemoteAddr());

        String signature = new JWTSigner(jwtSecret).sign(payload);

        this.setCookie(ENCODED_JWT, signature, resp, 60 * 60 * 24 * 365);

        this.createSession(req, resp, session);

        return signature;
    }

    private void createSession(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        session = req.getSession();
        session.setAttribute("user", this.genson.serialize(this.user));
        session.setAttribute("ip", req.getRemoteAddr());
        IUserDto userResponse = this.user;
//        Dispatcher.salt = this.user.getSalt();
        userResponse.setSalt("");
        userResponse.setPassword("");
        resp.setCharacterEncoding("UTF-8");
        resp.getOutputStream().print(this.genson.serialize(userResponse));
    }

    /**
     * Ajoute un cookie dans la réponse envoyé au navigateur client.
     *
     * @param name - le nom du cookie
     * @param value - la valeur contenue dans le cookie
     * @param response - la réponse du serveur
     * @param maxAge - le temps de vie du cookie (en ms)
     */
    private void setCookie(String name, String value, HttpServletResponse response, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
//        logger.info("setCookie : Valeur du cookie, modifiée.");
        response.addCookie(cookie);
    }

    /**
     * Ecrit la réponse à renvoyer.
     *
     * @param resp       est la resp.
     * @param msg        le message.
     * @param codeRetour le code de retour.
     * @throws IOException Signale qu'une exception d'I/O quelconque s'est produite.
     */
    private void sendResponse(HttpServletResponse resp, String msg, int codeRetour)
            throws IOException {
        resp.setStatus(codeRetour);
        resp.setContentType("text");
        byte[] msgBytes = msg.getBytes("UTF-8");
        resp.setContentLength(msgBytes.length);
        resp.setCharacterEncoding("utf-8");
        resp.getOutputStream().write(msgBytes);
    }
}
