package com.example.ca1_cse225

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ca1_cse225.ui.theme.Ca1_cse225Theme
import kotlinx.coroutines.delay

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val rating: Float,
    val ratingCount: Int,
    val price: String,
    val description: String
)

class Ca1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ca1_cse225Theme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
    } else {
        CourseListScreen()
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Student Course Hub",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "Start your journey today!", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen() {
    var isLoading by remember { mutableStateOf(true) }
    var courses by remember { mutableStateOf(emptyList<Course>()) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }

    LaunchedEffect(Unit) {
        delay(2000)
        courses = listOf(
            Course(1, "Java Programming Masterclass", "Prof. Amit", 4.5f, 1250, "₹499", "Master Java from basics to advanced. Learn OOPS, Multithreading, and more."),
            Course(2, "C Language for Beginners", "Dr. Khanna", 4.2f, 850, "₹299", "Build a strong foundation. Covers pointers, memory management, and file handling."),
            Course(3, "C++ Data Structures", "Mr. Sharma", 4.8f, 2100, "₹599", "Crack coding interviews! Detailed implementation of Linked Lists, Trees, and Graphs."),
            Course(4, "Advanced Java Concepts", "Ms. Priya", 4.6f, 420, "₹450", "Deep dive into Spring Boot, Hibernate, and modern Java frameworks."),
            Course(5, "C++ Competitive Coding", "Sandeep Jain", 4.9f, 3500, "₹699", "Advanced algorithm design and time complexity optimization for competitive platforms."),
            Course(6, "Python for Engineers", "Prof. Wilson", 4.0f, 600, "₹399", "Quick start with Python for data analysis and automation tasks."),
            Course(7, "Web Development (HTML/CSS)", "Raj Malhotra", 4.3f, 150, "₹199", "Learn to design beautiful responsive websites using modern web standards."),
            Course(8, "Database Management (SQL)", "Dr. Soni", 4.4f, 980, "₹350", "Master relational databases. Write complex queries and optimize database design."),
            Course(9, "Android with Jetpack Compose", "Student Dev", 5.0f, 55, "Free", "The modern way to build Android apps. Learn Declarative UI with Kotlin!"),
            Course(10, "Logic Building with C", "Prof. Gupta", 4.1f, 320, "₹250", "Focus on developing problem-solving skills through tough logical puzzles.")
        )
        isLoading = false
    }

    if (selectedCourse != null) {
        CourseDetailScreen(course = selectedCourse!!, onBack = { selectedCourse = null })
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Available Courses", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Fetching courses...")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(courses) { course ->
                            CourseItem(course, onClick = { selectedCourse = course })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: Course, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "By ${course.instructor}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomRatingBar(rating = course.rating, count = course.ratingCount)
                Text(
                    text = course.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(course: Course, onBack: () -> Unit) {
    var isEnrolled by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Instructor: ${course.instructor}", style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            CustomRatingBar(rating = course.rating, count = course.ratingCount)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "About this course",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (!isEnrolled) {
                Button(
                    onClick = { isEnrolled = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Enroll Now - ${course.price}", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "You are enrolled!", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Please rate this course:")
                        Spacer(modifier = Modifier.height(12.dp))
                        CustomRatingBar(
                            rating = userRating,
                            onRatingChanged = { userRating = it }
                        )
                        if (userRating > 0) {
                            Text(
                                text = "You rated: $userRating Stars",
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomRatingBar(
    rating: Float,
    count: Int? = null,
    maxStars: Int = 5,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val filled = i <= rating
            Icon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (filled) Color(0xFFFFB300) else Color.Gray,
                modifier = Modifier
                    .size(if (onRatingChanged != null) 36.dp else 18.dp)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i.toFloat()) }
                        } else {
                            Modifier
                        }
                    )
            )
        }
        if (count != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$rating ($count ratings)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
}
