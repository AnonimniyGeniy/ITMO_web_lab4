package org.lab4_web.controller;

import org.lab4_web.dto.UserDTO;
import org.lab4_web.model.AuthManager;
import org.lab4_web.model.User;
import org.lab4_web.repository.UserRepository;
import org.lab4_web.util.AuthStatus;
import org.lab4_web.util.JwtTokenUtil;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    @EJB
    private AuthManager authorizationManager;

    @EJB
    private JwtTokenUtil jwtTokenUtil;

    @EJB
    private UserRepository userRepository;

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTempLog() {
        JSONObject jsonResopnse = new JSONObject();
        jsonResopnse.append("login", "vova");
        jsonResopnse.append("password", "12345678");
        jsonResopnse.append("token", "qwerty123452281337");
        return Response.ok(jsonResopnse.toString()).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(User data) {
        AuthStatus authStatus = authorizationManager.authenticate(data);
        String error = "";
        if (authStatus == AuthStatus.AUTH_OK) {
            String token = jwtTokenUtil.generateToken(data);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.append("token", token);
            jsonResponse.append("username", data.getUsername());
            return Response.ok(jsonResponse.toString()).build();
        } else if (authStatus == AuthStatus.AUTH_WRONG_LOGIN) {
            error = "Такого пользователя не существует";
        } else if (authStatus == AuthStatus.AUTH_WRONG_PASSWORD) {
            error = "Неправильный пароль";
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", error);
        return Response.status(400).entity(jsonResponse.toString()).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User data) {
        AuthStatus authStatus;

        User dbUser = userRepository.getUserByUsername(data.getUsername());
        System.out.println(dbUser);
        if (dbUser != null) authStatus = AuthStatus.AUTH_WRONG_LOGIN;
        else authStatus = AuthStatus.AUTH_OK;

        if (authStatus == AuthStatus.AUTH_OK) {
            UserDTO dto = new UserDTO();
            dto.setUsername(data.getUsername());
            dto.setPassword(data.getPassword());
            userRepository.addUser(dto);
            return Response.ok().build();
        } else {
            String error = "Такой пользователь уже существует";
            JSONObject jsonResopnse = new JSONObject();
            jsonResopnse.put("message", error);
            return Response.status(400).entity(jsonResopnse.toString()).build();
        }

    }
}