Add-Type -AssemblyName System.IO.Compression.FileSystem

$stagingDir = Join-Path $PWD "staging_pptx"
if (Test-Path $stagingDir) { Remove-Item -Recurse -Force $stagingDir }
New-Item -ItemType Directory -Path $stagingDir -Force | Out-Null

# Extract template
[System.IO.Compression.ZipFile]::ExtractToDirectory((Join-Path $PWD "Pixel_Desk_Clock_Design_Evolution.pptx"), $stagingDir)

# 1. Update ppt/presentation.xml slide dimensions to 1272 x 660 px (in EMUs: 12115800 x 6286500)
$presXmlPath = Join-Path $stagingDir "ppt\presentation.xml"
$presXml = [xml](Get-Content $presXmlPath)
$sldSz = $presXml.SelectSingleNode("//*[local-name()='sldSz']")
if ($sldSz) {
    $sldSz.SetAttribute("cx", "12115800")
    $sldSz.SetAttribute("cy", "6286500")
}
$presXml.Save($presXmlPath)

# 2. Clear old media and copy our new 8 slice images into ppt/media
$mediaDir = Join-Path $stagingDir "ppt\media"
if (-not (Test-Path $mediaDir)) { New-Item -ItemType Directory -Path $mediaDir -Force | Out-Null }
Get-ChildItem $mediaDir | Remove-Item -Force

$slicesDir = Join-Path $PWD "slices"
$images = @(
    @{ File="01_outer_halo.png";           Name="01 - Outer Halo Frame";                   X=0;   Y=0;   W=1272; H=660; rId="rId2" },
    @{ File="07_landscape_hills_cat.png";  Name="07 - Landscape Horizon, Hills & Cat";     X=14;  Y=420; W=1244; H=230; rId="rId3" },
    @{ File="05_flip_clock_cards.png";     Name="05 - Flip Clock Cards (Hours & Minutes)"; X=324; Y=214; W=598;  H=248; rId="rId4" },
    @{ File="06_seconds_card.png";         Name="06 - Seconds Card";                       X=886; Y=346; W=125;  H=96;  rId="rId5" },
    @{ File="02_date_day_badges.png";      Name="02 - Date & Day Badges";                  X=76;  Y=54;  W=426;  H=130; rId="rId6" },
    @{ File="03_status_stack.png";         Name="03 - Status Stack (Speaker, Bell, AM-PM)";X=76;  Y=180; W=78;   H=224; rId="rId7" },
    @{ File="04_weather_widget_night.png"; Name="04 - Weather Widget (Night Moon)";        X=924; Y=53;  W=272;  H=197; rId="rId8" },
    @{ File="04_weather_widget_day.png";   Name="04 - Weather Widget (Day Sun)";           X=924; Y=53;  W=272;  H=197; rId="rId9" }
)

for ($i = 0; $i -lt $images.Count; $i++) {
    $img = $images[$i]
    $srcImg = Join-Path $slicesDir $img.File
    $destImg = Join-Path $mediaDir "image$($i+1).png"
    Copy-Item $srcImg $destImg -Force
}

# 3. Clean up extra slides in ppt/slides, keep only slide1.xml
$slidesDir = Join-Path $stagingDir "ppt\slides"
Get-ChildItem $slidesDir -Filter "slide*.xml" | Where-Object { $_.Name -ne "slide1.xml" } | Remove-Item -Force

$slidesRelsDir = Join-Path $stagingDir "ppt\slides\_rels"
Get-ChildItem $slidesRelsDir -Filter "slide*.xml.rels" | Where-Object { $_.Name -ne "slide1.xml.rels" } | Remove-Item -Force

# Update ppt/_rels/presentation.xml.rels to keep only slide1.xml
$presRelsPath = Join-Path $stagingDir "ppt\_rels\presentation.xml.rels"
$presRelsXml = [xml](Get-Content $presRelsPath)
$nsMgr = New-Object System.Xml.XmlNamespaceManager($presRelsXml.NameTable)
$nsMgr.AddNamespace("r", "http://schemas.openxmlformats.org/package/2006/relationships")

$slideRelNodes = $presRelsXml.SelectNodes("//r:Relationship[contains(@Type, 'slide') and not(contains(@Type, 'slideMaster'))]", $nsMgr)
foreach ($node in $slideRelNodes) {
    if ($node.GetAttribute("Target") -ne "slides/slide1.xml") {
        $node.ParentNode.RemoveChild($node) | Out-Null
    }
}
$presRelsXml.Save($presRelsPath)

# Update ppt/presentation.xml sldIdLst to keep only slide1
$sldIdLst = $presXml.SelectSingleNode("//*[local-name()='sldIdLst']")
if ($sldIdLst) {
    $firstSld = $sldIdLst.FirstChild
    $sldIdLst.RemoveAll()
    $sldIdLst.AppendChild($firstSld) | Out-Null
    $presXml.Save($presXmlPath)
}

# 4. Generate slide1.xml
$sbPic = New-Object System.Text.StringBuilder
$shapeId = 2
for ($i = 0; $i -lt $images.Count; $i++) {
    $img = $images[$i]
    $offX = $img.X * 9525
    $offY = $img.Y * 9525
    $extCx = $img.W * 9525
    $extCy = $img.H * 9525
    $rId = "rId$($i+2)"
    $name = $img.Name

    [void]$sbPic.AppendLine("  <p:pic>")
    [void]$sbPic.AppendLine("    <p:nvPicPr>")
    [void]$sbPic.AppendLine("      <p:cNvPr id=""$shapeId"" name=""$name""/>")
    [void]$sbPic.AppendLine("      <p:cNvPicPr><a:picLocks noChangeAspect=""1""/></p:cNvPicPr>")
    [void]$sbPic.AppendLine("      <p:nvPr/>")
    [void]$sbPic.AppendLine("    </p:nvPicPr>")
    [void]$sbPic.AppendLine("    <p:blipFill>")
    [void]$sbPic.AppendLine("      <a:blip r:embed=""$rId""/>")
    [void]$sbPic.AppendLine("      <a:stretch><a:fillRect/></a:stretch>")
    [void]$sbPic.AppendLine("    </p:blipFill>")
    [void]$sbPic.AppendLine("    <p:spPr>")
    [void]$sbPic.AppendLine("      <a:xfrm>")
    [void]$sbPic.AppendLine("        <a:off x=""$offX"" y=""$offY""/>")
    [void]$sbPic.AppendLine("        <a:ext cx=""$extCx"" cy=""$extCy""/>")
    [void]$sbPic.AppendLine("      </a:xfrm>")
    [void]$sbPic.AppendLine("      <a:prstGeom prst=""rect""><a:avLst/></a:prstGeom>")
    [void]$sbPic.AppendLine("    </p:spPr>")
    [void]$sbPic.AppendLine("  </p:pic>")
    $shapeId++
}

$slide1Content = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main">
  <p:cSld>
    <p:bg>
      <p:bgPr>
        <a:solidFill><a:srgbClr val="000000"/></a:solidFill>
        <a:effectLst/>
      </p:bgPr>
    </p:bg>
    <p:spTree>
      <p:nvGrpSpPr>
        <p:cNvPr id="1" name=""/>
        <p:cNvGrpSpPr/>
        <p:nvPr/>
      </p:nvGrpSpPr>
      <p:grpSpPr>
        <a:xfrm>
          <a:off x="0" y="0"/>
          <a:ext cx="0" cy="0"/>
          <a:chOff x="0" y="0"/>
          <a:chExt cx="0" cy="0"/>
        </a:xfrm>
      </p:grpSpPr>
$($sbPic.ToString())
    </p:spTree>
  </p:cSld>
  <p:clrMapOvr><a:masterClrMapping/></p:clrMapOvr>
</p:sld>
"@

$slide1Path = Join-Path $stagingDir "ppt\slides\slide1.xml"
[System.IO.File]::WriteAllText($slide1Path, $slide1Content.Trim(), [System.Text.Encoding]::UTF8)

# 5. Generate slide1.xml.rels
$sbRels = New-Object System.Text.StringBuilder
[void]$sbRels.AppendLine('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>')
[void]$sbRels.AppendLine('<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">')
[void]$sbRels.AppendLine('  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout" Target="../slideLayouts/slideLayout1.xml"/>')

for ($i = 0; $i -lt $images.Count; $i++) {
    $rId = "rId$($i+2)"
    $targetImg = "../media/image$($i+1).png"
    [void]$sbRels.AppendLine("  <Relationship Id=""$rId"" Type=""http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"" Target=""$targetImg""/>")
}
[void]$sbRels.AppendLine('</Relationships>')

$slide1RelsPath = Join-Path $stagingDir "ppt\slides\_rels\slide1.xml.rels"
[System.IO.File]::WriteAllText($slide1RelsPath, $sbRels.ToString(), [System.Text.Encoding]::UTF8)

# 6. Update [Content_Types].xml
$contentTypesPath = Join-Path $stagingDir "[Content_Types].xml"
$ctXml = [xml](Get-Content -LiteralPath $contentTypesPath)
$nsCt = New-Object System.Xml.XmlNamespaceManager($ctXml.NameTable)
$nsCt.AddNamespace("c", "http://schemas.openxmlformats.org/package/2006/content-types")

$extraSlides = $ctXml.SelectNodes("//c:Override[contains(@PartName, '/ppt/slides/slide') and @PartName != '/ppt/slides/slide1.xml']", $nsCt)
foreach ($node in $extraSlides) {
    $node.ParentNode.RemoveChild($node) | Out-Null
}

$pngDefault = $ctXml.SelectSingleNode("//c:Default[@Extension='png']", $nsCt)
if (-not $pngDefault) {
    $newDef = $ctXml.CreateElement("Default", "http://schemas.openxmlformats.org/package/2006/content-types")
    $newDef.SetAttribute("Extension", "png")
    $newDef.SetAttribute("ContentType", "image/png")
    $ctXml.DocumentElement.AppendChild($newDef) | Out-Null
}
$ctXml.Save($contentTypesPath)

# 7. Zip staging folder into Wandering_Watch_Grid_Layout.pptx
$outPptx = Join-Path $PWD "Wandering_Watch_Grid_Layout.pptx"
if (Test-Path $outPptx) { Remove-Item -Force $outPptx }
[System.IO.Compression.ZipFile]::CreateFromDirectory($stagingDir, $outPptx)

Remove-Item -Recurse -Force $stagingDir

Write-Host "Generated Wandering_Watch_Grid_Layout.pptx successfully!"
