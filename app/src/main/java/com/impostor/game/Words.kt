package com.impostor.game

/** Categorías y palabras. Añade o quita las que quieras: la app las coge solas. */
object Words {
    val categories: Map<String, List<String>> = mapOf(
        "Animales" to listOf(
            "Perro", "Gato", "Elefante", "Jirafa", "León", "Tigre", "Delfín", "Ballena",
            "Pingüino", "Águila", "Búho", "Serpiente", "Cocodrilo", "Canguro", "Koala",
            "Mono", "Gorila", "Oso", "Lobo", "Zorro", "Conejo", "Tortuga", "Caballo",
            "Vaca", "Cerdo", "Gallina", "Pulpo", "Tiburón", "Mariposa", "Hormiga",
            "Camello", "Pato",
        ),
        "Comida y bebida" to listOf(
            "Paella", "Tortilla de patatas", "Pizza", "Hamburguesa", "Sushi", "Tacos",
            "Helado", "Chocolate", "Café", "Cerveza", "Vino", "Gazpacho", "Jamón", "Queso",
            "Pan", "Arroz", "Pasta", "Ensalada", "Sandía", "Plátano", "Fresa", "Naranja",
            "Churros", "Croquetas", "Empanada", "Sopa", "Huevo frito", "Patatas fritas",
            "Palomitas", "Zumo", "Tarta", "Galletas",
        ),
        "Lugares" to listOf(
            "Playa", "Montaña", "Biblioteca", "Hospital", "Aeropuerto", "Supermercado",
            "Cine", "Museo", "Colegio", "Gimnasio", "Piscina", "Discoteca", "Iglesia",
            "Estadio", "Zoo", "Parque de atracciones", "Cementerio", "Banco", "Farmacia",
            "Panadería", "Restaurante", "Hotel", "Estación de tren", "Gasolinera",
            "Peluquería", "Cárcel", "Castillo", "Granja", "Selva", "Faro",
        ),
        "Profesiones" to listOf(
            "Médico", "Profesor", "Bombero", "Policía", "Cocinero", "Piloto", "Fontanero",
            "Electricista", "Abogado", "Arquitecto", "Peluquero", "Cantante", "Actor",
            "Futbolista", "Astronauta", "Carpintero", "Panadero", "Camarero", "Dentista",
            "Veterinario", "Periodista", "Fotógrafo", "Payaso", "Mago", "Cartero",
            "Taxista", "Soldado", "Escritor", "Pintor", "Granjero", "Detective", "Juez",
        ),
        "Objetos" to listOf(
            "Paraguas", "Llaves", "Cepillo de dientes", "Tijeras", "Martillo", "Lámpara",
            "Espejo", "Reloj", "Gafas", "Mochila", "Teléfono", "Ordenador", "Televisión",
            "Nevera", "Microondas", "Silla", "Cama", "Sofá", "Libro", "Bolígrafo",
            "Cuchara", "Tenedor", "Cuchillo", "Sartén", "Vela", "Maleta", "Almohada",
            "Toalla", "Linterna", "Cámara", "Guitarra", "Cargador",
        ),
        "Deportes" to listOf(
            "Fútbol", "Baloncesto", "Tenis", "Natación", "Ciclismo", "Boxeo", "Golf",
            "Rugby", "Voleibol", "Balonmano", "Esquí", "Surf", "Atletismo", "Gimnasia",
            "Judo", "Karate", "Ajedrez", "Pádel", "Béisbol", "Waterpolo", "Escalada",
            "Patinaje", "Equitación", "Esgrima", "Triatlón", "Hockey", "Tiro con arco",
            "Remo", "Yoga", "Maratón", "Skate", "Bádminton",
        ),
        "Países y ciudades" to listOf(
            "España", "Francia", "Italia", "Alemania", "Portugal", "México", "Argentina",
            "Brasil", "Japón", "China", "India", "Egipto", "Australia", "Canadá", "Rusia",
            "Madrid", "Barcelona", "París", "Londres", "Roma", "Nueva York", "Tokio",
            "Berlín", "Lisboa", "Venecia", "Ámsterdam", "Sídney", "Dubái", "El Cairo",
            "Buenos Aires", "Marrakech", "Estambul",
        ),
        "Transporte" to listOf(
            "Coche", "Autobús", "Tren", "Metro", "Avión", "Barco", "Bicicleta", "Moto",
            "Helicóptero", "Tranvía", "Patinete", "Camión", "Taxi", "Globo aerostático",
            "Submarino", "Cohete", "Ambulancia", "Tractor", "Velero", "Teleférico",
            "Furgoneta", "Ferry", "Caravana", "Grúa",
        ),
        "Naturaleza" to listOf(
            "Volcán", "Río", "Lago", "Cascada", "Bosque", "Isla", "Cueva", "Glaciar",
            "Arcoíris", "Tormenta", "Nieve", "Niebla", "Terremoto", "Huracán", "Desierto",
            "Oasis", "Acantilado", "Pradera", "Pantano", "Arrecife", "Luna", "Sol",
            "Estrella", "Nube", "Rayo", "Océano", "Valle", "Duna", "Géiser", "Manantial",
        ),
        "Ropa" to listOf(
            "Camiseta", "Pantalones", "Falda", "Vestido", "Chaqueta", "Abrigo", "Bufanda",
            "Guantes", "Gorro", "Calcetines", "Zapatillas", "Botas", "Sandalias", "Corbata",
            "Traje", "Pijama", "Bañador", "Bikini", "Cinturón", "Sombrero", "Gafas de sol",
            "Chándal", "Vaqueros", "Jersey", "Sudadera", "Delantal", "Bata", "Chaleco",
            "Tacones", "Pañuelo", "Gorra",
        ),
    )
}
