# Build Wandering_Watch_Grid_Layout.pptx using native PowerPoint COM automation
# This ensures 100% binary compatibility with PowerPoint and prevents any XML schema corruption.

$ErrorActionPreference = "Stop"

$slicesDir = Join-Path $PWD "slices"
$outPptx = Join-Path $PWD "Wandering_Watch_Grid_Layout.pptx"

# Ensure PowerPoint is closed before starting
Get-Process -Name POWERPNT -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Milliseconds 500

$ppt = New-Object -ComObject PowerPoint.Application

try {
    # 0 = Hidden / no window, 1 = With window
    $pres = $ppt.Presentations.Add(0)

    # 1. Slide Dimensions: Pixel 9a ratio at 1272 x 660 px (converted to Points: 1 px = 0.75 pt)
    $slideW = 1272 * 0.75 # 954 pt
    $slideH = 660 * 0.75  # 495 pt
    $pres.PageSetup.SlideWidth = $slideW
    $pres.PageSetup.SlideHeight = $slideH

    # Define slice components with pixel coordinates
    $layersCommon = @(
        @{ File="01_outer_halo.png";          Name="01 - Outer Halo Frame";                     X=0;   Y=0;   W=1272; H=660 },
        @{ File="07_landscape_hills_cat.png"; Name="07 - Landscape Horizon, Hills and Cat";     X=14;  Y=420; W=1244; H=230 },
        @{ File="05_flip_clock_cards.png";    Name="05 - Flip Clock Cards (Hours and Minutes)"; X=324; Y=214; W=598;  H=248 },
        @{ File="06_seconds_card.png";        Name="06 - Seconds Card";                         X=886; Y=346; W=125;  H=96  },
        @{ File="02_date_day_badges.png";     Name="02 - Date and Day Badges";                  X=76;  Y=54;  W=426;  H=130 },
        @{ File="03_status_stack.png";        Name="03 - Status Stack (Speaker, Bell, AM-PM)";  X=76;  Y=180; W=78;   H=224 }
    )

    $nightWeather = @{ File="04_weather_widget_night.png"; Name="04 - Weather Widget (Night Moon)"; X=924; Y=53; W=272; H=197 }
    $dayWeather   = @{ File="04_weather_widget_day.png";   Name="04 - Weather Widget (Day Sun)";    X=924; Y=53; W=272; H=197 }

    function SetupSlideBackground($slide) {
        $slide.FollowMasterBackground = 0 # msoFalse
        $slide.Background.Fill.Solid()
        $slide.Background.Fill.ForeColor.RGB = 0 # Pitch black
    }

    function AddLayer($slide, $layerDef) {
        $filePath = Join-Path $slicesDir $layerDef.File
        if (-not (Test-Path $filePath)) {
            throw "Slice file missing: $filePath"
        }
        $left = $layerDef.X * 0.75
        $top = $layerDef.Y * 0.75
        $width = $layerDef.W * 0.75
        $height = $layerDef.H * 0.75

        # Shapes.AddPicture(FileName, LinkToFile, SaveWithDocument, Left, Top, Width, Height)
        # LinkToFile = 0 (msoFalse), SaveWithDocument = -1 (msoTrue)
        $shp = $slide.Shapes.AddPicture($filePath, 0, -1, $left, $top, $width, $height)
        $shp.Name = $layerDef.Name
        $shp.LockAspectRatio = -1 # msoTrue
        return $shp
    }

    # ==========================================================
    # SLIDE 1: Night Layout (Full Interactive Composition)
    # ==========================================================
    $slide1 = $pres.Slides.Add(1, 12) # 12 = ppLayoutBlank
    SetupSlideBackground $slide1

    foreach ($layer in $layersCommon) {
        AddLayer $slide1 $layer | Out-Null
    }
    AddLayer $slide1 $nightWeather | Out-Null
    Write-Host "Slide 1 (Night Layout) populated with 7 movable layers."

    # ==========================================================
    # SLIDE 2: Day Layout (Full Interactive Composition)
    # ==========================================================
    $slide2 = $pres.Slides.Add(2, 12)
    SetupSlideBackground $slide2

    foreach ($layer in $layersCommon) {
        AddLayer $slide2 $layer | Out-Null
    }
    AddLayer $slide2 $dayWeather | Out-Null
    Write-Host "Slide 2 (Day Layout) populated with 7 movable layers."

    # ==========================================================
    # SLIDE 3: Component Palette & Reference Library
    # ==========================================================
    $slide3 = $pres.Slides.Add(3, 12)
    SetupSlideBackground $slide3

    # Add a title header
    $tb = $slide3.Shapes.AddTextbox(1, 40 * 0.75, 20 * 0.75, 800 * 0.75, 40 * 0.75)
    $tb.TextFrame.TextRange.Text = "WANDERING WATCH - COMPONENT SLICES PALETTE"
    $tb.TextFrame.TextRange.Font.Name = "Consolas"
    $tb.TextFrame.TextRange.Font.Size = 16
    $tb.TextFrame.TextRange.Font.Bold = -1
    $tb.TextFrame.TextRange.Font.Color.RGB = 0x00FFFFFF # White

    # Add all slices with both weather variants for copy-pasting
    $allSlices = @(
        @{ File="02_date_day_badges.png";     Name="Date and Day Badges";     X=40;  Y=80;  W=426; H=130 },
        @{ File="03_status_stack.png";        Name="Status Stack";            X=40;  Y=230; W=78;  H=224 },
        @{ File="05_flip_clock_cards.png";    Name="Flip Clock Cards";        X=140; Y=230; W=598; H=248 },
        @{ File="06_seconds_card.png";        Name="Seconds Card";            X=760; Y=230; W=125; H=96  },
        @{ File="04_weather_widget_night.png"; Name="Weather (Night Moon)";    X=920; Y=80;  W=272; H=197 },
        @{ File="04_weather_widget_day.png";   Name="Weather (Day Sun)";       X=920; Y=300; W=272; H=197 },
        @{ File="07_landscape_hills_cat.png"; Name="Landscape Hills and Cat"; X=40;  Y=500; W=1150; H=150 }
    )

    foreach ($item in $allSlices) {
        $p = Join-Path $slicesDir $item.File
        $scale = 0.75
        if ($item.File -eq "07_landscape_hills_cat.png") {
            # Scale slightly to fit palette slide
            $scale = 0.55
        }
        $shp = $slide3.Shapes.AddPicture($p, 0, -1, $item.X * 0.75, $item.Y * 0.75, $item.W * $scale, $item.H * $scale)
        $shp.Name = "Palette: " + $item.Name
    }
    Write-Host "Slide 3 (Palette Reference) created."

    # Save presentation
    if (Test-Path $outPptx) { Remove-Item -Force $outPptx }
    $pres.SaveAs($outPptx)
    $pres.Close()

    Write-Host "Successfully generated native PPTX at: $outPptx"

} finally {
    $ppt.Quit()
    [System.Runtime.InteropServices.Marshal]::ReleaseComObject($ppt) | Out-Null
    [System.GC]::Collect()
    [System.GC]::WaitForPendingFinalizers()
}
