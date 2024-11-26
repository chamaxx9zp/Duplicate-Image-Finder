
# Duplicate Image Finder using Perceptual Hashing

This Java program helps identify duplicate images using the **perceptual hashing algorithm**. The tool detects visually similar images, even if they differ in size, resolution, or compression, making it ideal for cleaning up image libraries or identifying redundant images.

The program also outputs the results to a **CSV file** in a table format, listing all the duplicate image pairs.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/chamaxx9zp/Duplicate-Image-Finder.git


## Usage

 - Update the Image Folder Path: Open the Main.java file and modify the path to point to your own sample image folder. This folder should contain the images you want to scan for duplicates.
 - Run the Program: After updating the path, run the Main.java file.
 - CSV Output: The program will generate a csv file in the current directory. This CSV file will contain two columns: the original image and its duplicate(s), side by side.


## Features

- Perceptual Hashing: Identifies duplicate or visually similar images, even if they have been resized, compressed, or edited.
- CSV Output: Detected duplicates are written in a CSV file, displaying original images and their duplicates side by side.
- Batch Processing: The program can scan entire folders of images
- Command-Line Interface: Run the program easily by just updating the image folder path and executing the Java program.

