package absortio.m00p4.negocio.service.singleton;


import absortio.m00p4.negocio.model.UsuarioModel;

public class UsuarioSingleton {
    public static UsuarioModel usuarioModel;
    private static UsuarioSingleton ourInstance = null;

    public static UsuarioSingleton getInstance() {
        if (ourInstance==null)
            ourInstance =new UsuarioSingleton();
        return ourInstance;
    }

    private UsuarioSingleton() {
    }

    public UsuarioModel getUsuarioModel() {
        return usuarioModel;
    }

    public void setUsuarioModel(UsuarioModel usuarioModel) {
        UsuarioSingleton.usuarioModel = usuarioModel;

    }
}
