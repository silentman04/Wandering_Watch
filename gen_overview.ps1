Add-Type -AssemblyName System.Drawing

$slicesDir = "C:\Users\manuv\Documents\antigravity\idle-pixel\slices"

$canvasW = 1400
$canvasH = 1000
$bmp = New-Object System.Drawing.Bitmap($canvasW, $canvasH)
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.Clear([System.Drawing.Color]::FromArgb(255, 18, 18, 22))

$fontTitle = New-Object System.Drawing.Font("Consolas", 14, [System.Drawing.FontStyle]::Bold)
$fontLabel = New-Object System.Drawing.Font("Consolas", 10, [System.Drawing.FontStyle]::Regular)
$brushWhite = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::White)
$brushMuted = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 180, 180, 190))
$penGrid = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(255, 45, 45, 55), 1)
$penBox = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(255, 80, 140, 255), 2)
$penBoxGreen = New-Object System.Drawing.Pen([System.Drawing.Color]::FromArgb(255, 60, 220, 120), 2)

# Draw Title
$g.DrawString('WANDERING WATCH - 7 MODULAR SLICE COMPONENTS (v7 MASTER ARTWORK)', $fontTitle, $brushWhite, 30, 20)
$g.DrawString('Resolution: 1272 x 660 px | PowerPoint file: Wandering_Watch_Grid_Layout.pptx', $fontLabel, $brushMuted, 30, 48)

# Top section: Assembled Preview (scaled to 50% width: 636 x 330)
$asmX = 40
$asmY = 85
$asmW = 636
$asmH = 330

$g.FillRectangle([System.Drawing.Brushes]::Black, $asmX, $asmY, $asmW, $asmH)
$g.DrawRectangle($penBoxGreen, $asmX, $asmY, $asmW, $asmH)
$g.DrawString('FULL ASSEMBLED COMPOSITE (OLED BLACK BASE)', $fontLabel, $brushWhite, $asmX + 10, $asmY - 20)

# Draw assembled layers
$dayPath = "C:\Users\manuv\Documents\antigravity\idle-pixel\app\src\main\res\drawable\clock_template_day.png"
$dayBmp = New-Object System.Drawing.Bitmap($dayPath)
$g.DrawImage($dayBmp, [System.Drawing.Rectangle]::new($asmX, $asmY, $asmW, $asmH), [System.Drawing.Rectangle]::new(0, 0, $dayBmp.Width, $dayBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$dayBmp.Dispose()

# Right of assembled preview: Component Cards Grid
$g.DrawString('1. OUTER HALO: 1272 x 660 at (0, 0)', $fontLabel, $brushWhite, 720, 85)
$g.DrawString('2. DATE AND DAY: 426 x 130 at (76, 54)', $fontLabel, $brushWhite, 720, 125)
$g.DrawString('3. STATUS STACK: 78 x 224 at (76, 180)', $fontLabel, $brushWhite, 720, 165)
$g.DrawString('4. WEATHER WIDGET: 272 x 197 at (924, 53)', $fontLabel, $brushWhite, 720, 205)
$g.DrawString('5. FLIP CLOCK CARDS: 598 x 248 at (324, 214)', $fontLabel, $brushWhite, 720, 245)
$g.DrawString('6. SECONDS CARD: 125 x 96 at (886, 346)', $fontLabel, $brushWhite, 720, 285)
$g.DrawString('7. LANDSCAPE HILLS AND CAT: 1244 x 230 at (14, 420)', $fontLabel, $brushWhite, 720, 325)

# Bottom section: The 7 Individual Extracted Transparent Slices
$g.DrawLine($penGrid, 30, 440, $canvasW - 30, 440)
$g.DrawString('INDIVIDUAL EXTRACTED SLICES (TRANSPARENT BACKGROUNDS IN ./slices/):', $fontTitle, $brushWhite, 30, 455)

# Slice 1: Weather Widget (Day & Night)
$wDay = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "04_weather_widget_day.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 40, 490, 160, 115)
$g.DrawRectangle($penBox, 40, 490, 160, 115)
$g.DrawImage($wDay, [System.Drawing.Rectangle]::new(40, 490, 160, 115), [System.Drawing.Rectangle]::new(0, 0, $wDay.Width, $wDay.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('04_weather_day', $fontLabel, $brushMuted, 40, 610)
$wDay.Dispose()

$wNight = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "04_weather_widget_night.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 220, 490, 160, 115)
$g.DrawRectangle($penBox, 220, 490, 160, 115)
$g.DrawImage($wNight, [System.Drawing.Rectangle]::new(220, 490, 160, 115), [System.Drawing.Rectangle]::new(0, 0, $wNight.Width, $wNight.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('04_weather_night', $fontLabel, $brushMuted, 220, 610)
$wNight.Dispose()

# Slice 2: Date & Day Badges
$ddBmp = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "02_date_day_badges.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 400, 490, 240, 75)
$g.DrawRectangle($penBox, 400, 490, 240, 75)
$g.DrawImage($ddBmp, [System.Drawing.Rectangle]::new(400, 490, 240, 75), [System.Drawing.Rectangle]::new(0, 0, $ddBmp.Width, $ddBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('02_date_day_badges', $fontLabel, $brushMuted, 400, 570)
$ddBmp.Dispose()

# Slice 3: Status Stack
$stBmp = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "03_status_stack.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 660, 490, 60, 172)
$g.DrawRectangle($penBox, 660, 490, 60, 172)
$g.DrawImage($stBmp, [System.Drawing.Rectangle]::new(660, 490, 60, 172), [System.Drawing.Rectangle]::new(0, 0, $stBmp.Width, $stBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('03_status', $fontLabel, $brushMuted, 660, 667)
$stBmp.Dispose()

# Slice 4: Flip Clock Cards
$fcBmp = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "05_flip_clock_cards.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 740, 490, 360, 150)
$g.DrawRectangle($penBox, 740, 490, 360, 150)
$g.DrawImage($fcBmp, [System.Drawing.Rectangle]::new(740, 490, 360, 150), [System.Drawing.Rectangle]::new(0, 0, $fcBmp.Width, $fcBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('05_flip_clock_cards', $fontLabel, $brushMuted, 740, 645)
$fcBmp.Dispose()

# Slice 5: Seconds Card
$scBmp = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "06_seconds_card.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 1120, 490, 100, 77)
$g.DrawRectangle($penBox, 1120, 490, 100, 77)
$g.DrawImage($scBmp, [System.Drawing.Rectangle]::new(1120, 490, 100, 77), [System.Drawing.Rectangle]::new(0, 0, $scBmp.Width, $scBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('06_seconds', $fontLabel, $brushMuted, 1120, 572)
$scBmp.Dispose()

# Slice 6: Landscape Hills & Cat
$lhBmp = New-Object System.Drawing.Bitmap((Join-Path $slicesDir "07_landscape_hills_cat.png"))
$g.FillRectangle([System.Drawing.Brushes]::Black, 40, 710, 1300, 240)
$g.DrawRectangle($penBox, 40, 710, 1300, 240)
$g.DrawImage($lhBmp, [System.Drawing.Rectangle]::new(40, 710, 1300, 240), [System.Drawing.Rectangle]::new(0, 0, $lhBmp.Width, $lhBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
$g.DrawString('07_landscape_hills_cat', $fontLabel, $brushMuted, 40, 955)
$lhBmp.Dispose()

$outPath = "C:\Users\manuv\Documents\antigravity\idle-pixel\slices_overview.png"
$bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)

$g.Dispose()
$bmp.Dispose()

Write-Host "Generated slices_overview.png"
