$csharp = @"
using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;

public class ImageSlicer
{
    public static void SliceAll(string basePath, string dayPath, string nightPath, string outDir)
    {
        Directory.CreateDirectory(outDir);

        using (Bitmap baseBmp = new Bitmap(basePath))
        using (Bitmap dayBmp = new Bitmap(dayPath))
        using (Bitmap nightBmp = new Bitmap(nightPath))
        {
            // 1. ZONE_OUTER_HALO (1272 x 660 at X=0, Y=0)
            // Center area (X: 46..1226, Y: 46..614) is made transparent!
            using (Bitmap halo = new Bitmap(baseBmp.Width, baseBmp.Height, PixelFormat.Format32bppArgb))
            {
                for (int y = 0; y < baseBmp.Height; y++)
                {
                    for (int x = 0; x < baseBmp.Width; x++)
                    {
                        Color c = baseBmp.GetPixel(x, y);
                        if (x >= 46 && x <= 1226 && y >= 46 && y <= 614)
                        {
                            halo.SetPixel(x, y, Color.FromArgb(0, 0, 0, 0));
                        }
                        else
                        {
                            halo.SetPixel(x, y, Color.FromArgb(255, c.R, c.G, c.B));
                        }
                    }
                }
                halo.Save(Path.Combine(outDir, "01_outer_halo.png"), ImageFormat.Png);
            }

            // 2. ZONE_DATE_DAY: X=76, Y=54, W=426, H=130
            CropWithTransparency(baseBmp, 76, 54, 426, 130, (sx, sy, c) =>
            {
                if (c.R > 20 || c.G > 20 || c.B > 20) return 255;
                if ((sx >= 80 && sx <= 270 && sy >= 58 && sy <= 165) ||
                    (sx >= 275 && sx <= 497 && sy >= 58 && sy <= 165)) return 255;
                return 0;
            }, Path.Combine(outDir, "02_date_day_badges.png"));

            // 3. ZONE_STATUS_STACK: X=76, Y=180, W=78, H=224
            CropWithTransparency(baseBmp, 76, 180, 78, 224, (sx, sy, c) =>
            {
                if (c.R > 20 || c.G > 20 || c.B > 20) return 255;
                if (sx >= 86 && sx <= 142 && sy >= 340 && sy <= 396) return 255;
                return 0;
            }, Path.Combine(outDir, "03_status_stack.png"));

            // 4. ZONE_WEATHER (DAY & NIGHT): X=924, Y=53, W=272, H=197
            Func<int, int, Color, int> weatherMask = (sx, sy, c) =>
            {
                if (sx >= 929 && sx <= 1191 && sy >= 58 && sy <= 245) return 255;
                if (c.R > 20) return 255;
                return 0;
            };
            CropWithTransparency(dayBmp, 924, 53, 272, 197, weatherMask, Path.Combine(outDir, "04_weather_widget_day.png"));
            CropWithTransparency(nightBmp, 924, 53, 272, 197, weatherMask, Path.Combine(outDir, "04_weather_widget_night.png"));

            // 5. ZONE_FLIP_CLOCK: X=324, Y=214, W=598, H=248
            CropWithTransparency(baseBmp, 324, 214, 598, 248, (sx, sy, c) =>
            {
                bool isCard = (sx >= 327 && sx <= 470 && sy >= 218 && sy <= 459) ||
                              (sx >= 476 && sx <= 619 && sy >= 218 && sy <= 459) ||
                              (sx >= 625 && sx <= 768 && sy >= 218 && sy <= 459) ||
                              (sx >= 774 && sx <= 919 && sy >= 218 && sy <= 459) ||
                              (sx >= 619 && sx <= 625 && c.R > 50);
                if (isCard) return 255;
                if (c.R > 30) return 255;
                return 0;
            }, Path.Combine(outDir, "05_flip_clock_cards.png"));

            // 6. ZONE_SECONDS: X=886, Y=346, W=125, H=96
            CropWithTransparency(baseBmp, 886, 346, 125, 96, (sx, sy, c) =>
            {
                if (sx >= 890 && sx <= 1007 && sy >= 350 && sy <= 438) return 255;
                if (c.R > 30) return 255;
                return 0;
            }, Path.Combine(outDir, "06_seconds_card.png"));

            // 7. ZONE_LANDSCAPE: X=14, Y=420, W=1244, H=230
            // Clear any flip clock card tips (X: 250..1015, Y <= 462) so landscape is 100% clean
            CropWithTransparency(baseBmp, 14, 420, 1244, 230, (sx, sy, c) =>
            {
                if (sx >= 250 && sx <= 1015 && sy <= 462) return 0;
                if (c.R > 15 || c.G > 15 || c.B > 15) return 255;
                return 0;
            }, Path.Combine(outDir, "07_landscape_hills_cat.png"));
        }
    }

    private static void CropWithTransparency(Bitmap src, int x, int y, int w, int h, Func<int, int, Color, int> mask, string outPath)
    {
        using (Bitmap crop = new Bitmap(w, h, PixelFormat.Format32bppArgb))
        {
            for (int cy = 0; cy < h; cy++)
            {
                for (int cx = 0; cx < w; cx++)
                {
                    int sx = x + cx;
                    int sy = y + cy;
                    if (sx >= 0 && sx < src.Width && sy >= 0 && sy < src.Height)
                    {
                        Color c = src.GetPixel(sx, sy);
                        int alpha = mask(sx, sy, c);
                        crop.SetPixel(cx, cy, Color.FromArgb(alpha, c.R, c.G, c.B));
                    }
                }
            }
            crop.Save(outPath, ImageFormat.Png);
        }
    }
}
"@

Add-Type -TypeDefinition $csharp -ReferencedAssemblies System.Drawing

$base = "C:\Users\manuv\Documents\antigravity\idle-pixel\app\src\main\res\drawable\clock_template_base.png"
$day = "C:\Users\manuv\Documents\antigravity\idle-pixel\app\src\main\res\drawable\clock_template_day.png"
$night = "C:\Users\manuv\Documents\antigravity\idle-pixel\app\src\main\res\drawable\clock_template_night.png"
$out = "C:\Users\manuv\Documents\antigravity\idle-pixel\slices"

$sw = [System.Diagnostics.Stopwatch]::StartNew()
[ImageSlicer]::SliceAll($base, $day, $night, $out)
$sw.Stop()
Write-Host "Sliced all 7 zones in $($sw.ElapsedMilliseconds) ms!"
