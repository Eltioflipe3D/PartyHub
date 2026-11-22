package com.partyhub.data

/**
 * Categorías y palabras compartidas por el Impostor y el juego de la frente.
 * Añade o quita las que quieras: los juegos las cogen solas.
 */
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
        "Cine y series" to listOf(
            "Titanic", "Shrek", "Harry Potter", "El Rey León", "Frozen", "Toy Story",
            "Star Wars", "Los Vengadores", "Matrix", "Jurassic Park", "El Señor de los Anillos",
            "Stranger Things", "Breaking Bad", "La Casa de Papel", "Juego de Tronos",
            "Los Simpson", "Friends", "Buscando a Nemo", "Aladdín", "Gladiator",
            "Piratas del Caribe", "Regreso al Futuro", "Mamma Mia", "Los Increíbles",
            "Coco", "Up", "El Padrino", "Tiburón", "Ocho apellidos vascos", "Los Goonies",
        ),
        "Famosos" to listOf(
            "Messi", "Cristiano Ronaldo", "Shakira", "Rosalía", "Bad Bunny", "Taylor Swift",
            "Beyoncé", "Einstein", "Picasso", "Napoleón", "Cleopatra", "Elon Musk",
            "Michael Jackson", "Freddie Mercury", "Leonardo DiCaprio", "Rafa Nadal",
            "Penélope Cruz", "Antonio Banderas", "Dua Lipa", "Cervantes", "Shakespeare",
            "Frida Kahlo", "Mozart", "Michael Jordan", "Marilyn Monroe", "Charles Chaplin",
            "Salvador Dalí", "Cristóbal Colón",
        ),
        "Personajes" to listOf(
            "Superman", "Spider-Man", "Batman", "Mario Bros", "Pikachu", "Sonic",
            "Homer Simpson", "Bob Esponja", "Pocoyó", "Darth Vader", "Gandalf", "Don Quijote",
            "Caperucita Roja", "Peter Pan", "Pinocho", "Blancanieves", "Papá Noel",
            "Drácula", "Sherlock Holmes", "Hulk", "Goku", "Winnie the Pooh", "Mickey Mouse",
            "Tarzán", "Cenicienta",
        ),
        "Acciones" to listOf(
            "Bailar", "Dormir", "Cocinar", "Nadar", "Correr", "Cantar", "Llorar", "Reír",
            "Conducir", "Pescar", "Leer", "Estornudar", "Bostezar", "Ducharse", "Afeitarse",
            "Planchar", "Fregar los platos", "Hacerse un selfie", "Jugar al fútbol",
            "Subir una montaña", "Hacer cola", "Pedir perdón", "Dar un abrazo", "Aplaudir",
            "Maquillarse", "Hacer yoga", "Tocar la guitarra", "Atarse los zapatos",
            "Montar en bici", "Hacer la compra", "Echarse la siesta", "Contar un chiste",
            "Pintar un cuadro", "Hacer un regalo",
        ),
    )
}
