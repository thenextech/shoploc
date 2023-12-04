package nextech.shoploc.controllers.auth;

/*
@Component
@WebFilter(urlPatterns = {"/*"})
public class UserAuthenticationFilter implements Filter {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        // Vérifier si la requête est pour la connexion ou l'inscription
        if (requestURI.endsWith("/login") || requestURI.endsWith("/register")) {
            chain.doFilter(request, response);
            return;
        }

        // Vérifier si l'URL doit être protégée
        if (isUrlToProtect(requestURI)) {
            String userType = sessionManager.getConnectedUserType(httpRequest);

            // Vérifier si l'utilisateur est authentifié
            if (userType == null) {
                sendUnauthorizedResponse(httpResponse, getRedirectUrl(userType), "Please log in to access this resource");
                return;
            }

            // Vérifier si l'utilisateur a les autorisations nécessaires
            if (!canUserAccessURL(userType, requestURI, httpRequest)) {
                sendUnauthorizedResponse(httpResponse, getRedirectUrl(userType), "You do not have permission to access this resource");
                return;
            }
        }

        // Si l'URL n'est pas protégée ou si l'utilisateur a les autorisations nécessaires, laisser passer la requête
        chain.doFilter(request, response);
    }

    private boolean canUserAccessURL(String userType, String requestURI, HttpServletRequest httpRequest) {
        if (UserTypes.admin.toString().equalsIgnoreCase(userType)) {
            return requestURI.startsWith(httpRequest.getContextPath() + "/admin/");
        } else if (UserTypes.client.toString().equalsIgnoreCase(userType)) {
            return requestURI.startsWith(httpRequest.getContextPath() + "/client/");
        } else if (UserTypes.merchant.toString().equalsIgnoreCase(userType)) {
            return requestURI.startsWith(httpRequest.getContextPath() + "/merchant/");
        }
        return false;
    }

    private boolean isUrlToProtect(String url) {
        return url.startsWith("/admin") || url.startsWith("/client") || url.startsWith("/merchant") && !url.startsWith("/merchant/activate");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String redirectUrl, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String responseBody = "{\"url\":\"" + redirectUrl + "\", \"message\":\"" + message + "\"}";
        response.getWriter().write(responseBody);
    }

    private String getRedirectUrl(String userType) {
        // Logique pour déterminer l'URL de redirection basée sur le type d'utilisateur
        if (UserTypes.client.toString().equalsIgnoreCase(userType)) {
            return "/client/login";
        } else if (UserTypes.merchant.toString().equalsIgnoreCase(userType)) {
            return "/merchant/login";
        } else {
            return "/client/login"; // Redirection par défaut
        }
    }

    @Override
    public void destroy() {
    }
}


 */