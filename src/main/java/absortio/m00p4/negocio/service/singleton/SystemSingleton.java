package absortio.m00p4.negocio.service.singleton;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.UsuarioModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemSingleton {
    public static String rute;
    public static Boolean mapaExist=false;
    private static SystemSingleton ourInstance = null;

    private static HashMap provinciasxdepartamento; //solo de lima
    private static HashMap<String, List<String>> distritosxprovincia; //solo de lima
    private static List<String> departamentos; //solo de lima
    private static List<String> unidadespeso;
    private static List<String> unidadesvol;
    private static MapaModel mapa;


    private static HashMap marcasxCategorias;
    private static HashMap<String, List<String>> modelosxMarcas;

    public static SystemSingleton getInstance() {
        if (ourInstance==null)
            ourInstance =new SystemSingleton();
        return ourInstance;
    }

    public static Boolean getMapaExist() {
        return mapaExist;
    }

    public static void setMapaExist(Boolean mapaExist) {
        SystemSingleton.mapaExist = mapaExist;
    }

    private SystemSingleton() {
    }

    public static String getRute() {
        return rute;
    }

    public static void setRute(String rute) {
        SystemSingleton.rute = rute;
    }

    public void cargardatalugares(){
        List<String> provinciasLima=new ArrayList<>();

        distritosxprovincia=new HashMap<String, List<String>>();

        provinciasLima.add("Barranca");
        List<String> distritosbarranca=new ArrayList<>();
        distritosbarranca.add("Barranca");
        distritosbarranca.add("Paramonga");
        distritosbarranca.add("Pativilca");
        distritosbarranca.add("Supe");
        distritosbarranca.add("Supe Puerto");
        distritosxprovincia.put("Barranca", distritosbarranca);

        provinciasLima.add("Cajatambo");
        List<String> distritoscajatambo=new ArrayList<>();
        distritoscajatambo.add("Manas");
        distritoscajatambo.add("Gorgor");
        distritoscajatambo.add("Huancapon");
        distritoscajatambo.add("Cajatambo");
        distritoscajatambo.add("Copa");
        distritosxprovincia.put("Cajatambo", distritoscajatambo);

        provinciasLima.add("Canta");
        List<String> distritoscanta=new ArrayList<>();
        distritoscanta.add("Canta");
        distritoscanta.add("Arahuay");
        distritoscanta.add("Huamantanga");
        distritoscanta.add("Huaros");
        distritoscanta.add("Lachaqui");
        distritoscanta.add("San Buenaventura");
        distritoscanta.add("Santa Rosa de Quives");
        distritosxprovincia.put("Canta", distritoscanta);

        provinciasLima.add("Canete");
        List<String> distritoscanete=new ArrayList<>();
        distritoscanete.add("Asia");
        distritoscanete.add("Calango");
        distritoscanete.add("Cerro Azul");
        distritoscanete.add("Chilca");
        distritoscanete.add("Coayllo");
        distritoscanete.add("Imperial");
        distritoscanete.add("Lunahuana");
        distritoscanete.add("Mala");
        distritoscanete.add("Nuevo Imperial");
        distritoscanete.add("Pacaran");
        distritoscanete.add("Quilmana");
        distritoscanete.add("San Antonio");
        distritoscanete.add("San Luis");
        distritoscanete.add("San Vicente de Canete");
        distritoscanete.add("Santa Cruz de Flores");
        distritoscanete.add("Zuniga");
        distritosxprovincia.put("Canete", distritoscanete);


        provinciasLima.add("Huaral");
        List<String> distritoshuaral=new ArrayList<>();
        distritoshuaral.add("27 de Noviembre");
        distritoshuaral.add("Altavillos alto");
        distritoshuaral.add("Altavillos bajo");
        distritoshuaral.add("Aucallama");
        distritoshuaral.add("Chancay");
        distritoshuaral.add("Huaral");
        distritoshuaral.add("Ihuari");
        distritoshuaral.add("Lampian");
        distritoshuaral.add("Pacaraos");
        distritoshuaral.add("Pacaran");
        distritoshuaral.add("Santa Cruz de Andamarca");
        distritoshuaral.add("Sumbilca");
        distritoshuaral.add("San Miguel de Acos");
        distritosxprovincia.put("Huaral", distritoshuaral);



        provinciasLima.add("Huarochiri");
        List<String> distritoshuarochiri=new ArrayList<>();
        distritoshuarochiri.add("Matucana");
        distritoshuarochiri.add("Antioquía");
        distritoshuarochiri.add("Callahuanca");
        distritoshuarochiri.add("Carampoma");
        distritoshuarochiri.add("Chicla");
        distritoshuarochiri.add("Cuenca");
        distritoshuarochiri.add("Huachupampa");
        distritoshuarochiri.add("Huanza");
        distritoshuarochiri.add("Huarochirí");
        distritoshuarochiri.add("Lahuaytambo");
        distritoshuarochiri.add("Langa");
        distritoshuarochiri.add("Laraos");
        distritoshuarochiri.add("Mariatana");
        distritoshuarochiri.add("Ricardo Palma");
        distritoshuarochiri.add("San Andrés de Tupicocha");
        distritoshuarochiri.add("San Antonio de Chaclla");
        distritoshuarochiri.add("San Damián");
        distritoshuarochiri.add("San Bartolomé");
        distritoshuarochiri.add("San Juan de Iris");
        distritoshuarochiri.add("San Juan de Tantaranche");
        distritoshuarochiri.add("San Lorenzo de Quinti");
        distritoshuarochiri.add("San Mateo");
        distritoshuarochiri.add("San Mateo de Otao");
        distritoshuarochiri.add("San Pedro de Casta");
        distritoshuarochiri.add("San Pedro de Huancayre");
        distritoshuarochiri.add("Sangallaya");
        distritoshuarochiri.add("Santa Cruz de Cocachacra");
        distritoshuarochiri.add("Santa Eulalia");
        distritoshuarochiri.add("Santiago de Anchucaya");
        distritoshuarochiri.add("Santiago de Tuna");
        distritoshuarochiri.add("Santo Domingo de los Olleros");
        distritoshuarochiri.add("San Jerónimo de Surco");
        distritosxprovincia.put("Huarochiri", distritoshuarochiri);


        provinciasLima.add("Huara");
        List<String> distritoshuara=new ArrayList<>();
        distritoshuara.add("Huacho");
        distritoshuara.add("Ámbar");
        distritoshuara.add("Caleta de Carquín");
        distritoshuara.add("Checras");
        distritoshuara.add("Hualmay");
        distritoshuara.add("Huaura");
        distritoshuara.add("Leoncio Prado");
        distritoshuara.add("Santa Leonor");
        distritoshuara.add("Paccho");
        distritoshuara.add("Santa María");
        distritoshuara.add("Sayán");
        distritoshuara.add("Vegeta");
        distritosxprovincia.put("Huara", distritoshuara);

        provinciasLima.add("Lima");
        List<String> distritoslima=new ArrayList<>();
        distritoslima.add("Lima");
        distritoslima.add("Ancón");
        distritoslima.add("Ate");
        distritoslima.add("Barranco");
        distritoslima.add("Breña");
        distritoslima.add("Carabayllo");
        distritoslima.add("Chaclacayo");
        distritoslima.add("Chorrillos");
        distritoslima.add("Cieneguilla");
        distritoslima.add("Comas");
        distritoslima.add("El Agustino");
        distritoslima.add("Independencia");
        distritoslima.add("Jesús María");
        distritoslima.add("La Molina");
        distritoslima.add("La Victoria");
        distritoslima.add("Lince");
        distritoslima.add("Los Olivos");
        distritoslima.add("Lurigancho-Chosica");
        distritoslima.add("Lurin");
        distritoslima.add("Magdalena del Mar");
        distritoslima.add("Pueblo Libre");
        distritoslima.add("Miraflores");
        distritoslima.add("Pachacámac");
        distritoslima.add("Pucusana");
        distritoslima.add("Puente Piedra");
        distritoslima.add("Punta Hermosa");
        distritoslima.add("Punta Negra");
        distritoslima.add("Rímac");
        distritoslima.add("San Bartolo");
        distritoslima.add("San Borja");
        distritoslima.add("San Isidro");
        distritoslima.add("San Juan de Lurigancho");
        distritoslima.add("San Juan de Miraflores");
        distritoslima.add("San Luis");
        distritoslima.add("San Martín de Porres");
        distritoslima.add("San Miguel");
        distritoslima.add("Santa Anita");
        distritoslima.add("Santa María del Mar");
        distritoslima.add("Santa Rosa");
        distritoslima.add("Santiago de Surco");
        distritoslima.add("Surquillo");
        distritoslima.add("Villa El Salvador");
        distritoslima.add("Villa María del Triunfo");
        distritosxprovincia.put("Lima", distritoslima);

        //provinciasLima.add("Oyon"); falta oyon

        //provinciasLima.add("Yauyos"); falta yauyos

        provinciasxdepartamento=new HashMap<String, List<String>>();
        provinciasxdepartamento.put("Lima", provinciasLima);

        departamentos=new ArrayList<String>();
        departamentos.add("Lima");
        departamentos.add(null);

    }

    public void cargargarcombospesoyvol(){
        unidadesvol=new ArrayList<>();
        unidadespeso=new ArrayList<>();
        unidadespeso.add("kg");
        unidadespeso.add("ton");
        unidadesvol.add("m3");
        unidadesvol.add("litro");
    }

    public  HashMap<String, List<String>> getProvinciasxdepartamento() {
        return provinciasxdepartamento;
    }

    public  void setProvinciasxdepartamento(HashMap<String, List<String>> provinciasxdepartamento) {
        SystemSingleton.provinciasxdepartamento = provinciasxdepartamento;
    }

    public  HashMap<String, List<String>> getDistritosxprovincia() {
        return distritosxprovincia;
    }

    public  void setDistritosxprovincia(HashMap<String, List<String>> distritosxprovincia) {
        SystemSingleton.distritosxprovincia = distritosxprovincia;
    }

    public  List<String> getDepartamentos() {
        return departamentos;
    }

    public  void setDepartamentos(List<String> departamentos) {
        SystemSingleton.departamentos = departamentos;
    }


    public static List<String> getUnidadespeso() {
        return unidadespeso;
    }

    public static void setUnidadespeso(List<String> unidadespeso) {
        SystemSingleton.unidadespeso = unidadespeso;
    }

    public static List<String> getUnidadesvol() {
        return unidadesvol;
    }

    public static void setUnidadesvol(List<String> unidadesvol) {
        SystemSingleton.unidadesvol = unidadesvol;
    }

    public static MapaModel getMapa() {
        return mapa;
    }

    public static void setMapa(MapaModel mapa) {
        SystemSingleton.mapa = mapa;
    }

    public static void cargarMarcasYModelos(){

    }
}                                                                                         

