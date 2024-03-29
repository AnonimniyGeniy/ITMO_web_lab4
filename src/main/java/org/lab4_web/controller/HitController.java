package org.lab4_web.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import org.lab4_web.annotation.Secured;
import org.lab4_web.dto.HitDTO;
import org.lab4_web.dto.UserDTO;
import org.lab4_web.model.Hit;
import org.lab4_web.model.HitChecker;
import org.lab4_web.repository.HitRepository;
import org.lab4_web.repository.UserRepository;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Path("/hits")
public class HitController {

    @EJB
    private UserRepository userRepository;

    @EJB
    private HitRepository hitRepository;

    @EJB
    private HitChecker hitChecker;

    @Secured
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveHit(@Context SecurityContext sc, Hit hit) {
        LocalDateTime start = LocalDateTime.now();
        HitDTO dto = new HitDTO();
        dto.setCurrentTime(new Timestamp(new Date().getTime()));
        dto.setX(hit.getX());
        dto.setY(hit.getY());
        dto.setR(hit.getR());
        UserDTO user = userRepository.getUserDTOByUsername(
                sc.getUserPrincipal().getName()
        );
        dto.setUser(user);
        dto.setHit(hitChecker.checkHit(hit));
        dto.setExecuteTime(Math.round(LocalDateTime.now().minusNanos(start.getNano()).getNano() * 0.001));

        hitRepository.saveHit(dto);



        return Response.ok().entity(new JSONObject(dto).toString()).build();

    }

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHits(@Context SecurityContext sc) {
//        System.out.println(sc.getUserPrincipal().getName());
//        List<HitDTO> arr = new ArrayList<>();
//        JSONArray jsonResponse = new JSONArray();
//        jsonResponse.put(arr);
//        return Response.ok().entity(jsonResponse.toString()).build();
        String username = sc.getUserPrincipal().getName();
        UserDTO user = userRepository.getUserDTOByUsername(username);
        List<HitDTO> hits = hitRepository.getHits(user);
        return Response.ok().entity(new JSONArray(hits).toString()).build();
    }

    @Secured
    @Path("/clear")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response clearHits(@Context SecurityContext sc) {
        UserDTO user = userRepository.getUserDTOByUsername(sc.getUserPrincipal().getName());
        int hits = hitRepository.clearHits(user);
        return Response.ok(hits).build();
    }
}