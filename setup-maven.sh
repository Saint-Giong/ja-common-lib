#!/bin/bash

# Setup Maven for GitHub Packages Authentication
# This script configures ~/.m2/settings.xml to allow Maven to download packages from GitHub Packages

set -e

echo "🔧 Maven GitHub Packages Setup Script"
echo "======================================="
echo ""

# Check if settings.xml already exists
SETTINGS_FILE="$HOME/.m2/settings.xml"
BACKUP_FILE="$HOME/.m2/settings.xml.backup-$(date +%Y%m%d-%H%M%S)"

# Prompt for GitHub credentials
echo "Please provide your GitHub credentials:"
echo ""
read -p "GitHub Username: " GITHUB_USERNAME

if [ -z "$GITHUB_USERNAME" ]; then
    echo "❌ Error: GitHub username cannot be empty"
    exit 1
fi

echo ""
echo "Generate a GitHub Personal Access Token (PAT):"
echo "1. Go to: https://github.com/settings/tokens"
echo "2. Click 'Generate new token (classic)'"
echo "3. Give it a name (e.g., 'Maven Package Access')"
echo "4. Select scope: 'read:packages'"
echo "5. Generate and copy the token"
echo ""
read -sp "GitHub Personal Access Token: " GITHUB_TOKEN
echo ""

if [ -z "$GITHUB_TOKEN" ]; then
    echo "❌ Error: GitHub token cannot be empty"
    exit 1
fi

# Create ~/.m2 directory if it doesn't exist
mkdir -p "$HOME/.m2"

# Backup existing settings.xml if it exists
if [ -f "$SETTINGS_FILE" ]; then
    echo ""
    echo "⚠️  Existing settings.xml found. Creating backup at:"
    echo "   $BACKUP_FILE"
    cp "$SETTINGS_FILE" "$BACKUP_FILE"
fi

# Create new settings.xml
echo ""
echo "📝 Creating Maven settings.xml..."

cat > "$SETTINGS_FILE" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>github</id>
      <username>${GITHUB_USERNAME}</username>
      <password>${GITHUB_TOKEN}</password>
    </server>
  </servers>

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo.maven.apache.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/saint-giong/ja-common-lib</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

</settings>
EOF

# Verify the file was created properly
if [ -f "$SETTINGS_FILE" ]; then
    echo "✅ Maven settings.xml created successfully!"
    echo ""
    echo "Location: $SETTINGS_FILE"
    echo ""
    
    # Test if XML is valid (if xmllint is available)
    if command -v xmllint &> /dev/null; then
        if xmllint --noout "$SETTINGS_FILE" 2>/dev/null; then
            echo "✅ XML syntax is valid"
        else
            echo "⚠️  Warning: XML syntax validation failed"
        fi
    fi
    
    echo ""
    echo "🎉 Setup complete! You can now build microservices that depend on ja-common-lib"
    echo ""
    echo "Test with:"
    echo "  cd ../ja-authentication-service"
    echo "  mvn clean install -U -DskipTests"
else
    echo "❌ Error: Failed to create settings.xml"
    exit 1
fi
