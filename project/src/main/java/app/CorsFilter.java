/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsFilter extends OncePerRequestFilter {
 

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

if(request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
String originHeader = request.getHeader("Origin");

response.addHeader("Access-Control-Allow-Origin", originHeader);
 
response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
response.addHeader("Access-Control-Allow-Headers", "Content-Type");
response.addHeader("Access-Control-Max-Age", "1800");
}
 
filterChain.doFilter(request, response);
}
}