package br.com.matheushbmelo.todolist.filters;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.matheushbmelo.todolist.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {
            // Pegar a autenticação enviada pela requisição
            var autenticacao = request.getHeader("Authorization");

            var auth_encoded = autenticacao.substring("Basic".length()).trim();

            byte[] auth_decode = Base64.getDecoder().decode(auth_encoded);

            var authString = new String(auth_decode);

            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            // Validar o usuario
            var user = userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuario sem autorização");
            } else {

                // Validar a senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {

                    // Autorizar ou não a requisição
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Usuario sem autorização");
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
