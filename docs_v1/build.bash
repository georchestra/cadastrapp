#! /bin/bash

# purge des builds
# pour voir toutes les erreurs à chaque build
rm -rf _build/

sphinx-build -b html ./ _build/

echo ""
sed -i -e 's/max-width:800px/max-width:1200px/g' _build/_static/css/theme.css
echo "maj CSS faite"
