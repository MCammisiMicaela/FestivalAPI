package com.festival.error;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorErrores {
    public record ErrorApi(LocalDateTime fecha, int estado, String mensaje, String ruta) { }

    private ResponseEntity<ErrorApi> respuesta(HttpStatus estado, String mensaje, HttpServletRequest request) {
        return ResponseEntity.status(estado).body(new ErrorApi(LocalDateTime.now(), estado.value(), mensaje, request.getRequestURI()));
    }

    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<ErrorApi> noEncontrado(RecursoNoEncontrado ex, HttpServletRequest req) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler({Conflicto.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorApi> conflicto(Exception ex, HttpServletRequest req) {
        String mensaje = ex instanceof Conflicto ? ex.getMessage() : "La operacion viola una restriccion de datos";
        return respuesta(HttpStatus.CONFLICT, mensaje, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .distinct().collect(Collectors.joining("; "));
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorApi> parametroInvalido(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "Parametro invalido: " + ex.getName(), req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApi> malCuerpo(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "JSON invalido o tipo de dato no reconocido", req);
    }
}
